package com.vuryss.aoc.client;

import com.vuryss.aoc.Configuration;
import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.PuzzleDayReference;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ReactiveAdventOfCodeClient implements AdventOfCodeClient {
    private static class Holder {
        public static final ReactiveAdventOfCodeClient INSTANCE = new ReactiveAdventOfCodeClient();
    }

    private static final long minDurationBetweenRequestsNs = Duration.ofSeconds(5).toNanos();
    private static final AtomicLong nextRequestTimeNs = new AtomicLong(0);

    private final HttpClient httpClient;

    @Setter
    private NotificationOutputInterface output;

    private ReactiveAdventOfCodeClient() {
        httpClient = HttpClient.create()
            .baseUrl("https://adventofcode.com")
            .headers(h -> h.set(HttpHeaderNames.USER_AGENT, "github.com/vuryss/aoc-java by vuryss@gmail.com"))
            .responseTimeout(Duration.ofSeconds(5));
    }

    private HttpClient authenticatedHttpClient() {
        String sessionToken = Configuration.getSessionToken();
        Cookie sessionCookie = new DefaultCookie("session", sessionToken);
        sessionCookie.setDomain(".adventofcode.com");
        sessionCookie.setSecure(true);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");

        return httpClient.cookie(sessionCookie);
    }

    public static ReactiveAdventOfCodeClient instance() {
        return Holder.INSTANCE;
    }

    public Mono<String> getPlayerInput(PuzzleDayReference puzzle) {
        return throttle(() -> {
            notice("Downloading input for event %s, day %s".formatted(puzzle.event(), puzzle.day()));

            return authenticatedHttpClient()
                .get()
                .uri("/%s/day/%s/input".formatted(puzzle.event(), puzzle.day()))
                .responseSingle((response, body) ->
                    body.asString()
                        .defaultIfEmpty("")
                        .flatMap(responseBody -> {
                            if (response.status().code() != 200) {
                                error("Error response body: " + responseBody);

                                return Mono.error(new RuntimeException(
                                    "Failed to get input (status code: " + response.status().code() + ") Check your session token and try again."
                                ));
                            }

                            if (responseBody.contains("Puzzle inputs differ by user.  Please log in to get your puzzle input.")) {
                                return Mono.error(new RuntimeException("Invalid session token, cannot get user input"));
                            }

                            return Mono.just(responseBody);
                        })
                );
        });
    }

    public Mono<Answers> getCorrectAnswers(PuzzleDayReference puzzle) {
        return throttle(() -> {
            notice("Downloading answers for event %s, day %s".formatted(puzzle.event(), puzzle.day()));

            return authenticatedHttpClient()
                .get()
                .uri("/%s/day/%s".formatted(puzzle.event(), puzzle.day()))
                .responseSingle((response, body) ->
                    body.asString()
                        .defaultIfEmpty("")
                        .flatMap(responseBody -> {
                            if (response.status().code() != 200) {
                                error("Error response body: " + responseBody);

                                return Mono.error(new RuntimeException(
                                    "Failed to get input (status code: " + response.status().code() + ") Check your session token and try again."
                                ));
                            }

                            var elements = Jsoup.parse(responseBody)
                                .select("p:containsOwn(Your puzzle answer was) > code");

                            if (elements.size() == 2) {
                                return Mono.just(new Answers(
                                    elements.get(0).text(),
                                    elements.get(1).text()
                                ));
                            }

                            // Last day there would be just 1 answer
                            if (elements.size() == 1 && responseBody.contains("Both parts of this puzzle are complete!")) {
                                return Mono.just(new Answers(elements.getFirst().text(), "Merry Christmas!"));
                            }

                            // Only part 1 solved
                            if (elements.size() == 1) {
                                return Mono.just(new Answers(elements.getFirst().text(), null));
                            }

                            return Mono.error(new RuntimeException(
                                "Cannot resolve answers for event %s day %s (found %s submitted answers)".formatted(
                                    puzzle.event(), puzzle.day(), elements.size()
                                )
                            ));
                        })
                );
        });
    }

    @SuppressWarnings("ReactiveStreamsThrowInOperator")
    public Mono<String> getDayDescription(PuzzleDayReference puzzle) {
        return throttle(() -> {
            notice("Downloading description for event %s, day %s".formatted(puzzle.event(), puzzle.day()));

            return authenticatedHttpClient()
                .get()
                .uri("/%s/day/%s".formatted(puzzle.event(), puzzle.day()))
                .responseSingle((response, body) ->
                    body.asString()
                        .defaultIfEmpty("")
                        .map(responseBody -> {
                            if (response.status().code() != 200) {
                                error("Error response body: " + responseBody);

                                throw new RuntimeException(
                                    "Failed to get description for event %s, day %s (status code: %s)".formatted(
                                        puzzle.event(),
                                        puzzle.day(),
                                        response.status().code()
                                    )
                                );
                            }

                            var articles = Jsoup.parse(responseBody).select("article.day-desc");

                            if (articles.isEmpty()) throw new RuntimeException(
                                "No puzzle description found for event %s, day %s".formatted(puzzle.event(), puzzle.day())
                            );

                            return articles.stream().map(this::convertHtmlToJavadoc).collect(Collectors.joining("\n\n"));
                        })
                );
        });
    }

    private String convertHtmlToJavadoc(Element element) {
        element.select("h2").forEach(
            e -> e.replaceWith(new TextNode(wrapTextMaxWidth(e.text())))
        );
        element.select("pre").forEach(
            e -> e.prepend("@NEW_LINE@")
        );
        element.select("p").forEach(
            e -> e.replaceWith(new TextNode(wrapTextMaxWidth(e.text())))
        );
        element.select("br").forEach(
            e -> e.replaceWith(new TextNode(wrapTextMaxWidth(e.text())))
        );

        element.select("li").forEach(li -> li.prepend("@NEW_LINE@- "));

        for (var node: element.childNodes()) {
            if (node instanceof TextNode && ((TextNode) node).isBlank()) {
                node.remove();
            }
        }

        return element.text().replace("@NEW_LINE@", "\n").trim();
    }

    private <T> Mono<T> throttle(Supplier<? extends Mono<? extends T>> supplier) {
        return Mono.deferContextual(context -> {
            Consumer<Status> onStatusChange = context.getOrDefault(Status.class, _ -> {});
            assert onStatusChange != null;
            onStatusChange.accept(Status.QUEUED);

            return waitUntilNextAllowedTimeSlot()
                .then(Mono.fromRunnable(() -> onStatusChange.accept(Status.DOWNLOADING)))
                .then(Mono.defer(supplier))
                .doOnSuccess(_ -> onStatusChange.accept(Status.COMPLETED))
                .doOnError(_ -> onStatusChange.accept(Status.FAILED));
        });
    }

    private String wrapTextMaxWidth(String text) {
        var words = text.split(" ");
        var wrappedText = new StringBuilder();
        var currentLine = new StringBuilder();

        for (var word: words) {
            if (currentLine.length() + word.length() + 1 > 117) {
                wrappedText.append(currentLine.toString().trim()).append("@NEW_LINE@");
                currentLine.setLength(0);
            }

            currentLine.append(word).append(" ");
        }

        wrappedText.append(currentLine);

        return "@NEW_LINE@@NEW_LINE@" + wrappedText.toString().trim();
    }

    private Mono<Void> waitUntilNextAllowedTimeSlot() {
        return Mono.defer(() -> {
            long now = System.nanoTime();
            long nextRequestNs = nextRequestTimeNs.getAndUpdate(
                previous -> Math.max(previous, now) + minDurationBetweenRequestsNs
            );
            long delay = Math.max(0, nextRequestNs - now);

            if (delay == 0) return Mono.empty();

            return Mono.delay(Duration.ofNanos(delay)).then();
        });
    }

    private void notice(String message) {
        if (output == null) return;

        output.notice(message);
    }

    private void error(String message) {
        if (output == null) return;

        output.error(message);
    }
}

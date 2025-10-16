package com.vuryss.aoc.client;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class NativeJavaAdventOfCodeClient implements AdventOfCodeClient {
    private final String sessionToken;

    public NativeJavaAdventOfCodeClient(
        @ConfigProperty(name = "session.token", defaultValue = "NONE") String sessionToken
    ) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String getPlayerInput(int year, int day) {
        var sessionCookie = new HttpCookie("session", sessionToken);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        var cookieManager = new CookieManager();
        cookieManager.getCookieStore().add(URI.create("https://adventofcode.com"), sessionCookie);

        var httpClient = HttpClient.newBuilder()
            .cookieHandler(cookieManager)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        var request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("https://adventofcode.com/%s/day/%s/input", year, day)))
            .header("User-Agent", "github.com/vuryss/aoc-java by vuryss@gmail.com")
            .GET()
            .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        httpClient.close();

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                "Failed to get input (status code: " + response.statusCode() + ") Check your session token and try again."
            );
        }

        var responseBody = response.body();

        if (responseBody.contains("Puzzle inputs differ by user.  Please log in to get your puzzle input.")) {
            throw new RuntimeException("Invalid session token");
        }

        return responseBody;
    }

    public Answers getCorrectAnswers(int eventYear, int eventDay) {
        var sessionCookie = new HttpCookie("session", sessionToken);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        var cookieManager = new CookieManager();
        cookieManager.getCookieStore().add(URI.create("https://adventofcode.com"), sessionCookie);

        var httpClient = HttpClient.newBuilder()
            .cookieHandler(cookieManager)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        var request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("https://adventofcode.com/%s/day/%s", eventYear, eventDay)))
            .header("User-Agent", "github.com/vuryss/aoc-java by vuryss@gmail.com")
            .GET()
            .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        httpClient.close();

        var elements = Jsoup.parse(response.body()).select("article+p > code");

        if (elements.size() != 2 && eventDay < 25) {
            throw new RuntimeException("Cannot resolve answers for year " + eventYear + " day " + eventDay);
        } else if (elements.size() != 1 && eventDay == 25) {
            throw new RuntimeException("Cannot resolve answers for year " + eventYear + " day " + eventDay);
        }

        var answer1 = elements.getFirst().text();

        if (eventDay == 25) {
            return new Answers(answer1, "Merry Christmas!");
        }

        var answer2 = elements.get(1).text();

        return new Answers(answer1, answer2);
    }

    public String getDayDescription(int year, int day) {
        var sessionCookie = new HttpCookie("session", sessionToken);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        var cookieManager = new CookieManager();
        cookieManager.getCookieStore().add(URI.create("https://adventofcode.com"), sessionCookie);

        var httpClient = HttpClient.newBuilder()
            .cookieHandler(cookieManager)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        var request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("https://adventofcode.com/%s/day/%s", year, day)))
            .header("User-Agent", "github.com/vuryss/aoc-java by vuryss@gmail.com")
            .GET()
            .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        httpClient.close();

        var elements = Jsoup.parse(response.body()).select("article.day-desc");
        var description = new StringBuilder();

        for (var element: elements) {
            if (!description.isEmpty()) {
                description.append("\n\n");
            }

            description.append(convertHtmlToJavadoc(element));
        }

        return description.toString();
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
}

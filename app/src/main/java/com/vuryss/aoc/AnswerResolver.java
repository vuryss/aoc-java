package com.vuryss.aoc;

import io.github.cdimascio.dotenv.Dotenv;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

public class AnswerResolver {
    final Path answerCacheDirectory = Path.of("src/main/resources/answer");

    public Map<Integer, String> getForEventDay(int eventYear, int eventDay) {
        var cachedAnswers = getCachedAnswers(eventYear, eventDay);

        if (cachedAnswers != null) {
            System.out.print("(cached answers)");
            return cachedAnswers;
        }

        System.out.print("(downloading answers)");
        return downloadForEventDay(eventYear, eventDay);
    }

    private Map<Integer, String> downloadForEventDay(int eventYear, int eventDay) {
        var sessionCookie = new HttpCookie("session", getSessionToken());
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

        var answer1 = elements.get(0).text();
        cacheAnswer(answer1, eventYear, eventDay, 1);

        if (eventDay == 25) {
            return Map.of(1, answer1, 2, "Merry Christmas!");
        }

        var answer2 = elements.get(1).text();
        cacheAnswer(answer2, eventYear, eventDay, 2);

        return Map.of(1, answer1, 2, answer2);
    }

    private Map<Integer, String> getCachedAnswers(int eventYear, int eventDay) {
        var answer1 = getCachedAnswer(eventYear, eventDay, 1);
        var answer2 = getCachedAnswer(eventYear, eventDay, 2);

        if (answer1 == null || answer2 == null) {
            return null;
        }

        return Map.of(1, answer1, 2, answer2);
    }

    private String getCachedAnswer(int eventYear, int eventDay, int part) {
        if (eventDay == 25 && part == 2) {
            return "Merry Christmas!";
        }

        var answerPath = Path.of(String.format("%s/%s-%s-part-%s-answer.txt", answerCacheDirectory, eventYear, eventDay, part));

        if (!Files.exists(answerPath)) {
            return null;
        }

        try {
            return Files.readString(answerPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cacheAnswer(String answer, int eventYear, int eventDay, int part) {
        if (!Files.exists(answerCacheDirectory)) {
            try {
                Files.createDirectory(answerCacheDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.write(
                Path.of(String.format("%s/%s-%s-part-%s-answer.txt", answerCacheDirectory, eventYear, eventDay, part)),
                answer.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSessionToken() {
        var dotenv = Dotenv
            .configure()
            .directory("./../") // .env file is in the root of the project, not in the app module
            .load();
        var sessionToken = dotenv.get("SESSION_TOKEN");

        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new RuntimeException("SESSION_TOKEN not found in .env file");
        }

        return sessionToken;
    }
}

package com.vuryss.aoc;

import io.github.cdimascio.dotenv.Dotenv;

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

public class InputResolver {
    final Path inputCacheDirectory = Path.of("src/main/resources/input");

    public String getForEventDay(int eventYear, int eventDay) {
        var cachedInput = getCachedInput(eventYear, eventDay);

        if (cachedInput != null) {
            System.out.println("Using cached puzzle input.");
            return cachedInput;
        }

        System.out.println("Downloading puzzle input from AoC.");
        return downloadForEventDay(eventYear, eventDay);
    }

    private String getCachedInput(int eventYear, int eventDay) {
        var inputCacheFile = Path.of(String.format("%s/%s-%s-input.txt", inputCacheDirectory, eventYear, eventDay));

        if (!Files.exists(inputCacheFile)) {
            return null;
        }

        try {
            return Files.readString(inputCacheFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String downloadForEventDay(int eventYear, int eventDay) {
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
            .uri(URI.create(String.format("https://adventofcode.com/%s/day/%s/input", eventYear, eventDay)))
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

        cacheInput(response, eventYear, eventDay);

        return response.body();
    }

    private void cacheInput(HttpResponse<String> response, int eventYear, int eventDay) {
        if (!Files.exists(inputCacheDirectory)) {
            try {
                Files.createDirectory(inputCacheDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.write(
                Path.of(String.format("%s/%s-%s-input.txt", inputCacheDirectory, eventYear, eventDay)),
                response.body().getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSessionToken() {
        var dotenv = Dotenv.load();
        var sessionToken = dotenv.get("SESSION_TOKEN");

        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new RuntimeException("SESSION_TOKEN not found in .env file");
        }

        return sessionToken;
    }
}

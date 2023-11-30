package com.vuryss.aoc;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class InputResolver {
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

        return response.body();
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

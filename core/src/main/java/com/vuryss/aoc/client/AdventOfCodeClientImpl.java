package com.vuryss.aoc.client;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@ApplicationScoped
public class AdventOfCodeClientImpl implements AdventOfCodeClient {
    private final String sessionToken;

    public AdventOfCodeClientImpl(
        @ConfigProperty(name = "session.token") String sessionToken
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

        return response.body();
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
}

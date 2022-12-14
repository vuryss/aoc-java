package com.vuryss.aoc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class InputResolver {
    @Value("${app.sessionToken}")
    String sessionToken;

    @Cacheable("userInput")
    public String downloadForEventDay(int eventYear, int eventDay) {
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

        return response.body();
    }
}

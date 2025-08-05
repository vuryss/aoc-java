package com.vuryss.aoc.client;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AdventOfCodeClientImpl implements AdventOfCodeClient {
    private final String sessionToken;

    public AdventOfCodeClientImpl(
        @ConfigProperty(name = "session.token") String sessionToken
    ) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String downloadInput(int year, int day) {
        return "" + sessionToken;
    }

    @Override
    public String downloadAnswer(int year, int day, int part) {
        return "" + sessionToken;
    }
}

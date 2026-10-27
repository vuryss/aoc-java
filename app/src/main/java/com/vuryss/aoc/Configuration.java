package com.vuryss.aoc;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    private static final Map<Integer, Integer> MAX_CHALLENGES_PER_YEAR = new HashMap<>();
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    static {
        // Historical years with 25 challenges each (December 1-25)
        MAX_CHALLENGES_PER_YEAR.put(2015, 25);
        MAX_CHALLENGES_PER_YEAR.put(2016, 25);
        MAX_CHALLENGES_PER_YEAR.put(2017, 25);
        MAX_CHALLENGES_PER_YEAR.put(2018, 25);
        MAX_CHALLENGES_PER_YEAR.put(2019, 25);
        MAX_CHALLENGES_PER_YEAR.put(2020, 25);
        MAX_CHALLENGES_PER_YEAR.put(2021, 25);
        MAX_CHALLENGES_PER_YEAR.put(2022, 25);
        MAX_CHALLENGES_PER_YEAR.put(2023, 25);
        MAX_CHALLENGES_PER_YEAR.put(2024, 25);

        // Starting from 2025, AoC will have 12 challenges per day (December 1-12)
        MAX_CHALLENGES_PER_YEAR.put(2025, 12);
    }

    public static int getMaxChallengesForYear(int year) {
        Integer maxChallenges = MAX_CHALLENGES_PER_YEAR.get(year);

        if (maxChallenges == null) {
            throw new IllegalArgumentException("Year " + year + " is not supported");
        }

        return maxChallenges;
    }

    public static boolean isValidDayForYear(int year, int day) {
        try {
            int maxDay = getMaxChallengesForYear(year);
            return day >= 1 && day <= maxDay;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static List<Integer> getSupportedEvents() {
        return MAX_CHALLENGES_PER_YEAR.keySet().stream().sorted().collect(Collectors.toList());
    }

    public static String getSessionToken() {
        // Check system env first
        String sessionToken = System.getenv("SESSION_TOKEN");

        if (sessionToken != null) return sessionToken;

        // Load it .env file
        sessionToken = dotenv.get("SESSION_TOKEN", null);

        if (sessionToken != null) return sessionToken;

        throw new RuntimeException("SESSION_TOKEN is not set, cannot authenticate with Advent of Code");
    }
}

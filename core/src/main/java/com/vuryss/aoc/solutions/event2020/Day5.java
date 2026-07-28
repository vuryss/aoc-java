package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return input.trim().lines().mapToInt(
            s -> Integer.parseInt(s.replace("F", "0").replace("L", "0").replace("B", "1").replace("R", "1"), 2)
        ).max().getAsInt() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var seats = input.trim().lines().mapToInt(
            s -> Integer.parseInt(s.replace("F", "0").replace("L", "0").replace("B", "1").replace("R", "1"), 2)
        ).sorted().toArray();

        Arrays.sort(seats);

        for (var i = 0; i < seats.length; i++)
            if (seats[i] == seats[i+1] - 2) return (seats[i] + 1) + "";

        return "-not-found-";
    }
}

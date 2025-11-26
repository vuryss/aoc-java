package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0/2
            2/2
            2/3
            3/4
            3/5
            0/1
            10/1
            9/10
            """,
            "31"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0/2
            2/2
            2/3
            3/4
            3/5
            0/1
            10/1
            9/10
            """,
            "19"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = parseParts(input);

        return String.valueOf(findMaxStrength(
            parts,
            0,
            new boolean[parts.length],
            0,
            new int[parts.length + 1],
            0
        ));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = parseParts(input);
        var maxStrengthPerLength = new int[parts.length + 1];

        findMaxStrength(
            parts,
            0,
            new boolean[parts.length],
            0,
            maxStrengthPerLength,
            0
        );

        for (var i = parts.length - 1; i >= 0; i--) {
            if (maxStrengthPerLength[i] > 0) {
                return String.valueOf(maxStrengthPerLength[i]);
            }
        }

        return "-not found-";
    }

    private Part[] parseParts(String input) {
        var lines = input.trim().split("\n");
        var parts = new Part[lines.length];

        for (var i = 0; i < lines.length; i++) {
            var ints = lines[i].split("/");
            parts[i] = new Part(Integer.parseInt(ints[0]), Integer.parseInt(ints[1]));
        }

        return parts;
    }

    private int findMaxStrength(Part[] parts, int port, boolean[] used, int strength, int[] maxStrengthPerLength, int length) {
        var maxStrength = strength;
        maxStrengthPerLength[length] = Math.max(maxStrengthPerLength[length], strength);

        for (var i = 0; i < parts.length; i++) {
            if (used[i] || parts[i].a != port && parts[i].b != port) {
                continue;
            }

            used[i] = true;
            maxStrength = Math.max(
                maxStrength,
                findMaxStrength(
                    parts,
                    parts[i].a == port ? parts[i].b : parts[i].a,
                    used,
                    strength + parts[i].a + parts[i].b,
                    maxStrengthPerLength,
                    length + 1
                )
            );
            used[i] = false;
        }

        return maxStrength;
    }

    record Part(int a, int b) {}
}

package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.IntUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """,
            "8"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """,
            "2286"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        int sum = 0;

        for (var line: lines) {
            var parts = line.split(": ");
            var gameId = IntUtil.parseUnsignedInteger(parts[0]);
            var colorCounts = calculateMaximumColorOccurrences(parts[1]);

            if (colorCounts.red <= 12 && colorCounts.green <= 13 && colorCounts.blue <= 14) {
                sum += gameId;
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        int sum = 0;

        for (var line: lines) {
            var parts = line.split(": ");
            var colorCounts = calculateMaximumColorOccurrences(parts[1]);

            sum += colorCounts.red * colorCounts.green * colorCounts.blue;
        }

        return String.valueOf(sum);
    }

    private ColorCount calculateMaximumColorOccurrences(String game) {
        var sets = game.split("; ");
        var colorCounts = new ColorCount();

        for (var set: sets) {
            var colors = set.split(", ");

            for (var color: colors) {
                var colorParts = color.split(" ");
                var colorName = colorParts[1];
                var cubeCount = Integer.parseInt(colorParts[0]);

                switch (colorName) {
                    case "red" -> colorCounts.red = Math.max(colorCounts.red, cubeCount);
                    case "green" -> colorCounts.green = Math.max(colorCounts.green, cubeCount);
                    case "blue" -> colorCounts.blue = Math.max(colorCounts.blue, cubeCount);
                }
            }
        }

        return colorCounts;
    }

    private static class ColorCount {
        public int red = 0;
        public int green = 0;
        public int blue = 0;
    }
}

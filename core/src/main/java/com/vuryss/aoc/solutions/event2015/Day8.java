package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ""
            "abc"
            "aaa\\"aaa"
            "\\x27"
            """,
            "12"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ""
            "abc"
            "aaa\\"aaa"
            "\\x27"
            """,
            "19"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var sum = 0;

        for (var line: lines) {
            var trimmedLine = line
                .substring(1, line.length() - 1)
                .replaceAll("\\\\\\\\", "\\\\")
                .replaceAll("\\\\x[0-9a-f]{2}", "x")
                .replaceAll("\\\\\"", "\"");

            sum += line.length() - trimmedLine.length();
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var sum = 0;

        for (var line: lines) {
            var escapedLine = line
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"");
            escapedLine = "\"" + escapedLine + "\"";

            sum += escapedLine.length() - line.length();
        }

        return String.valueOf(sum);
    }
}

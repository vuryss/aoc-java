package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.Map;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "1", "2",
            "11", "2",
            "21", "4",
            "1211", "6",
            "111221", "6"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        input = input.trim();
        var iterations = isTest ? 1 : 40;

        for (var i = 0; i < iterations; i++) {
            input = lookAndSay(input);
        }

        return String.valueOf(input.length());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        var iterations = 50;

        for (var i = 0; i < iterations; i++) {
            input = lookAndSay(input);
        }

        return String.valueOf(input.length());
    }

    private String lookAndSay(String input) {
        var lastChar = input.charAt(0);
        var count = 1;
        var output = new StringBuilder();

        for (var i = 1; i < input.length(); i++) {
            if (input.charAt(i) == lastChar) {
                count++;
            } else {
                output.append(count).append(lastChar);
                lastChar = input.charAt(i);
                count = 1;
            }
        }

        output.append(count).append(lastChar);

        return output.toString();
    }
}

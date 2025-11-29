package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayDeque;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("dabAcCaCBAcCcaDA", "10");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("dabAcCaCBAcCcaDA", "4");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return String.valueOf(react(input.trim()).length());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var polymer = input.trim();
        var shortestPolymerLength = Integer.MAX_VALUE;

        for (var c = 'a'; c <= 'z'; c++) {
            var newPolymer = polymer
                    .replaceAll(String.valueOf(c), "")
                    .replaceAll(String.valueOf(Character.toUpperCase(c)), "");
            var reactedPolymer = react(newPolymer);

            if (reactedPolymer.length() < shortestPolymerLength) {
                shortestPolymerLength = reactedPolymer.length();
            }
        }

        return String.valueOf(shortestPolymerLength);
    }

    private String react(String polymer) {
        var stack = new ArrayDeque<Character>(polymer.length());

        for (var c : polymer.toCharArray()) {
            if (!stack.isEmpty() && Math.abs(stack.peek() - c) == 32) {
                stack.pop();
            } else {
                stack.push(c);
            }
        }

        var sb = new StringBuilder();

        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }

        return sb.reverse().toString();
    }
}

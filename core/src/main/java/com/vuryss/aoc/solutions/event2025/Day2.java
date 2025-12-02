package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
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
        var inputs = input.trim().split(",");
        var sum = 0L;

        for (var range: inputs) {
            var parts = range.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);

            for (var i = start; i <= end; i++) {
                String n = String.valueOf(i);
                if (n.length() % 2 == 0 && n.substring(0, n.length() / 2).equals(n.substring(n.length() / 2))) {
                    sum += i;
                }
            }
        }
        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var inputs = input.trim().split(",");
        var sum = 0L;

        for (var range: inputs) {
            var parts = range.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);

            for (var i = start; i <= end; i++) {
                String n = String.valueOf(i);

                if (n.matches("^(\\d+)\\1{1,}")) {
                    sum += i;
                }
            }
        }

        return String.valueOf(sum);
    }
}

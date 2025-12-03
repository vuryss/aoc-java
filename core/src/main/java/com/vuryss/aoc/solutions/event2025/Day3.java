package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """,
            "357"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """,
            "3121910778619"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var sum = 0L;

        for (var line: lines) {
            sum += findMaximumJoltage(line, 2);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var sum = 0L;

        for (var line: lines) {
            sum += findMaximumJoltage(line, 12);
        }

        return String.valueOf(sum);
    }

    private long findMaximumJoltage(String bank, int size) {
        var batteries = bank.toCharArray();
        char[] maxBatteryCombination = new char[size];
        int foundBatteries = 0, position = 0;

        while (foundBatteries < size) {
            int n = 0;
            int leftBatteries = size - foundBatteries;

            for (var i = position; i < batteries.length - leftBatteries + 1; i++) {
                if (batteries[i] - '0' > n) {
                    n = batteries[i] - '0';
                    position = i + 1;
                }
            }

            maxBatteryCombination[foundBatteries++] = (char) (n + '0');
        }

        return Long.parseLong(new String(maxBatteryCombination));
    }
}

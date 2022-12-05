package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;

import java.util.Arrays;
import java.util.Map;

public class Day4 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
            """,
            "4"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.split("\n");
        var count = 0;

        for (var line: lines) {
            var pair = line.split(",");
            var range1 = Arrays.stream(pair[0].split("-")).mapToInt(Integer::parseInt).toArray();
            var range2 = Arrays.stream(pair[1].split("-")).mapToInt(Integer::parseInt).toArray();

            if (
                range1[0] <= range2[0] && range1[1] >= range2[1]
                || range1[0] >= range2[0] && range1[1] <= range2[1]
            ) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.split("\n");
        var count = 0;

        for (var line: lines) {
            var pair = line.split(",");
            var range1 = Arrays.stream(pair[0].split("-")).mapToInt(Integer::parseInt).toArray();
            var range2 = Arrays.stream(pair[1].split("-")).mapToInt(Integer::parseInt).toArray();

            if (range1[0] <= range2[1] && range1[1] >= range2[0]) {
                count++;
            }
        }

        return String.valueOf(count);
    }
}

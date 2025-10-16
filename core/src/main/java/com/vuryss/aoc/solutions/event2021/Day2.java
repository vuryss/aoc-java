package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.Map;


@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
            """,
            "150"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
            """,
            "900"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var horizontal = 0;
        var depth = 0;

        for (var line: lines) {
            var parts = line.split(" ");
            var value = Integer.parseInt(parts[1]);

            switch (parts[0]) {
                case "forward" -> horizontal += value;
                case "down" -> depth += value;
                case "up" -> depth -= value;
            }
        }

        return String.valueOf(horizontal * depth);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var horizontal = 0;
        var depth = 0;
        var aim = 0;

        for (var line: lines) {
            var parts = line.split(" ");
            var value = Integer.parseInt(parts[1]);

            switch (parts[0]) {
                case "forward" -> {
                    horizontal += value;
                    depth += aim * value;
                }
                case "down" -> aim += value;
                case "up" -> aim -= value;
            }
        }

        return String.valueOf(horizontal * depth);
    }
}

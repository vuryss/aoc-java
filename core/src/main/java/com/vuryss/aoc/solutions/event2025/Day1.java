package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """,
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """,
            "6"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var rotations = input.trim().split("\n");
        int counter = 0, position = 50, distance;
        char direction;

        for (var rotation: rotations) {
            direction = rotation.charAt(0);
            distance = Integer.parseInt(rotation.substring(1));

            position = direction == 'L' ? (position - distance) % 100 : (position + distance) % 100;

            if (position == 0) {
                counter++;
            }
        }

        return String.valueOf(counter);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var rotations = input.trim().split("\n");
        int counter = 0, position = 50, distance;
        char direction;

        for (var rotation: rotations) {
            direction = rotation.charAt(0);
            distance = Integer.parseInt(rotation.substring(1));

            for (var i = 0; i < distance; i++) {
                position = direction == 'L' ? (position - 1) % 100 : (position + 1) % 100;

                if (position == 0) {
                    counter++;
                }
            }
        }

        return String.valueOf(counter);
    }
}

package com.vuryss.aoc.solutions;

import java.util.Map;
import java.util.Objects;

public interface SolutionInterface {
    Map<String, String> part1Tests();
    Map<String, String> part2Tests();
    String part1Solution(String input, boolean isTest);
    String part2Solution(String input, boolean isTest);

    default String solve(int part, String input, boolean isTest) {
        try {
            var result = switch (part) {
                case 1 -> part1Solution(input, isTest);
                case 2 -> part2Solution(input, isTest);
                default -> throw new IllegalStateException("Invalid part: " + part);
            };

            return Objects.requireNonNull(result);
        } catch (Error e) {
            // Reactor drops fatal Errors instead of emitting onError, so wrap them in an Exception
            throw new RuntimeException(e);
        }
    }
}

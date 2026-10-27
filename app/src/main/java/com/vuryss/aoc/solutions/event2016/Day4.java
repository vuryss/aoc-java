package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            aaaaa-bbb-z-y-x-123[abxyz]
            a-b-c-d-e-f-g-h-987[abcde]
            not-a-real-room-404[oarel]
            totally-real-room-200[decoy]
            """,
            "1514"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            a-b-c-d-e-f-g-h-987[abcde]
            ijmockjgz-jwezxo-nojmvbz-993[jozmb]
            """,
            "993"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var sum = 0;

        for (var line: lines) {
            var parts = Objects.requireNonNull(Regex.matchGroups("^(.+)-([0-9]+)\\[([a-z]+)\\]", line));
            var hash = hash(parts.getFirst());

            if (hash.equals(parts.getLast())) {
                sum += Integer.parseInt(parts.get(1));
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");

        for (var line: lines) {
            var parts = Objects.requireNonNull(Regex.matchGroups("^(.+)-([0-9]+)", line));

            var result = parts.getFirst().chars()
                .map(c -> c == '-' ? ' ' : c) // Replace dashes with spaces
                .map(c -> c == ' ' ? ' ' : c + Integer.parseInt(parts.get(1)) % 26) // Shift the character
                .map(c -> c > 'z' ? c - 26 : c) // Wrap around if necessary
                .mapToObj(c -> (char) c)
                .map(Objects::toString)
                .collect(Collectors.joining());

            if (result.contains("north")) {
                return parts.get(1);
            }
        }

        return "-not found-";
    }

    public String hash(String string) {
        return StringUtil.tally(string.replace("-", "")).entrySet().stream()
            .sorted(
                Map.Entry.<Character, Integer>comparingByValue(Comparator.reverseOrder())
                    .thenComparing(Map.Entry.comparingByKey())
            )
            .limit(5)
            .map(Map.Entry::getKey)
            .map(String::valueOf)
            .collect(Collectors.joining());
    }
}

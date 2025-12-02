package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    private static final Pattern PATTERN_2 = Pattern.compile("^(\\d+)\\1+$");

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124",
            "1227775554"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124",
            "4174379265"
        );
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
                if (PATTERN_2.matcher(String.valueOf(i)).find()) {
                    sum += i;
                }
            }
        }

        return String.valueOf(sum);
    }
}

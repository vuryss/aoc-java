package com.vuryss.aoc.solutions.event2017;

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
            "1122", "3",
            "1111", "4",
            "1234", "0",
            "91212129", "9"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "1212", "6",
            "1221", "0",
            "123425", "4",
            "123123", "12",
            "12131415", "4"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var sum = 0;
        var line = input.trim();

        for (var i = 0; i < line.length(); i++) {
            if (line.charAt(i) == line.charAt((i + 1) % line.length())) {
                sum += Character.getNumericValue(line.charAt(i));
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var sum = 0;
        var line = input.trim();
        var size = line.length();
        var half = size / 2;

        for (var i = 0; i < size; i++) {
            if (line.charAt(i) == line.charAt((i + half) % size)) {
                sum += Character.getNumericValue(line.charAt(i));
            }
        }

        return String.valueOf(sum);
    }
}

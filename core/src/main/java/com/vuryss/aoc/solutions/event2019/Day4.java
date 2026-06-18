package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
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
        var parts = StringUtil.ints(input);
        return IntStream.range(parts.getFirst(), parts.getLast() + 1).filter(this::validPart1).count() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = StringUtil.ints(input);
        return IntStream.range(parts.getFirst(), parts.getLast() + 1).filter(this::validPart2).count() + "";
    }

    private boolean validPart1(int number) {
        int lastDigit = 10;
        boolean hasDouble = false;

        for (var i = 0; i < 6; i++) {
            int digit = number % 10;
            number /= 10;

            if (digit > lastDigit) return false;
            if (digit == lastDigit) hasDouble = true;
            lastDigit = digit;
        }

        return hasDouble;
    }

    private boolean validPart2(int number) {
        boolean hasDouble = false;
        var digits = new int[6];

        for (var i = 0; i < 6; i++) {
            int digit = number % 10;
            number /= 10;
            digits[i] = digit;
        }

        for (var i = 1; i < 6; i++) {
            if (digits[i] > digits[i - 1]) return false;
            if (digits[i] == digits[i - 1]
                && (i == 1 || digits[i - 2] != digits[i])
                && (i == 5 || digits[i + 1] != digits[i])
            ) hasDouble = true;
        }

        return hasDouble;
    }
}

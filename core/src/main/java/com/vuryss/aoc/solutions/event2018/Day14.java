package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "9", "5158916779",
            "5", "0124515891",
            "18", "9251071085",
            "2018", "5941429882"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "51589", "9",
            "01245", "5",
            "92510", "18",
            "59414", "2018"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var number = Integer.parseInt(input.trim());
        var scores = new int[10000000];
        scores[0] = 3;
        scores[1] = 7;
        var elf1 = 0;
        var elf2 = 1;
        int nextIndex = 2;

        while (nextIndex < number + 10) {
            var sum = scores[elf1] + scores[elf2];

            if (sum >= 10) {
                scores[nextIndex++] = 1;
            }

            scores[nextIndex++] = sum % 10;
            elf1 = (elf1 + scores[elf1] + 1) % nextIndex;
            elf2 = (elf2 + scores[elf2] + 1) % nextIndex;
        }

        var result = new StringBuilder();

        for (var i = number; i < number + 10; i++) {
            result.append(scores[i]);
        }

        return result.toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        int[] number = StringUtil.toDigitArray(input.trim());
        var scores = new int[100_000_000];
        scores[0] = 3;
        scores[1] = 7;
        var elf1 = 0;
        var elf2 = 1;
        int nextIndex = 2;

        while (true) {
            var sum = scores[elf1] + scores[elf2];

            if (sum >= 10) {
                scores[nextIndex++] = 1;

                if (match(scores, number, nextIndex)) {
                    return String.valueOf(nextIndex - number.length);
                }
            }

            scores[nextIndex++] = sum % 10;

            if (match(scores, number, nextIndex)) {
                return String.valueOf(nextIndex - number.length);
            }

            elf1 = (elf1 + scores[elf1] + 1) % nextIndex;
            elf2 = (elf2 + scores[elf2] + 1) % nextIndex;
        }
    }

    private boolean match(int[] scores, int[] number, int nextScoreIndex) {
        if (nextScoreIndex < number.length) {
            return false;
        }

        return Arrays.equals(scores, nextScoreIndex - number.length, nextScoreIndex, number, 0, number.length);
    }
}

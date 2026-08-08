package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            35
            20
            15
            25
            47
            40
            62
            55
            65
            95
            102
            117
            150
            182
            127
            219
            299
            277
            309
            576
            """,
            "127"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            35
            20
            15
            25
            47
            40
            62
            55
            65
            95
            102
            117
            150
            182
            127
            219
            299
            277
            309
            576
            """,
            "62"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        Long[] numbers = StringUtil.longs(input).toArray(new Long[]{});
        final var preamble = isTest ? 5 : 25;

        return findNumber(numbers, preamble).toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        Long[] numbers = StringUtil.longs(input).toArray(new Long[]{});
        final var preamble = isTest ? 5 : 25;
        final var needle = findNumber(numbers, preamble);

        for (var i = 0; i < numbers.length - 1; i++) {
            long acc = numbers[i];
            long min = acc, max = acc;

            for (var j = i + 1; j < numbers.length; j++) {
                acc += numbers[j];
                min = Math.min(min, numbers[j]);
                max = Math.max(max, numbers[j]);

                if (acc == needle) return (min + max) + "";
            }
        }

        return "-not-found-";
    }

    private Long findNumber(Long[] numbers, int preamble) {
        for (var i = preamble; i < numbers.length; i++) {
            var found = false;

            search:
            for (var j = i - preamble; j < i - 1; j++) {
                for (var k = j + 1; k < i; k++) {
                    if (numbers[j] + numbers[k] == numbers[i]) {
                        found = true;
                        break search;
                    }
                }
            }

            if (!found) return numbers[i];
        }

        return -1L;
    }
}

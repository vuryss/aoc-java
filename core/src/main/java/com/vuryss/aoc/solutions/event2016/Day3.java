package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            5 10 25
            """,
            "0"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            101 301 501
            102 302 502
            103 303 503
            201 401 601
            202 402 602
            203 403 603
            """,
            "6"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var count = 0;

        for (var line: lines) {
            var numbers = StringUtil.ints(line);

            if (isValidTriangle(numbers.get(0), numbers.get(1), numbers.get(2))) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var count = 0;

        for (var i = 0; i < lines.length; i += 3) {
            var numbersLine1 = StringUtil.ints(lines[i]);
            var numbersLine2 = StringUtil.ints(lines[i + 1]);
            var numbersLine3 = StringUtil.ints(lines[i + 2]);

            for (var j = 0; j < numbersLine1.size(); j++) {
                if (isValidTriangle(numbersLine1.get(j), numbersLine2.get(j), numbersLine3.get(j))) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private boolean isValidTriangle(int a, int b, int c) {
        return a + b > c && a + c > b && b + c > a;
    }
}

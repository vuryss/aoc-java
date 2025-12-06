package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s
            """,
            "4277556"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s
            """,
            "3263827"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        long sum = 0;
        var lists = new ArrayList<List<Integer>>();
        var lines = input.split("\n");
        var operations = Regex.matchAll("\\S", lines[lines.length - 1]);

        for (var i = 0; i < lines.length - 1; i++) {
            var numbers = StringUtil.ints(lines[i]);

            for (var j = 0; j < numbers.size(); j++) {
                if (j >= lists.size()) {
                    lists.add(new ArrayList<>());
                }

                lists.get(j).add(numbers.get(j));
            }
        }

        for (var i = 0; i < operations.size(); i++) {
            var operationNumbers = lists.get(i).stream().mapToLong(Integer::longValue);

            sum += operations.get(i).equals("+")
                ? operationNumbers.sum()
                : operationNumbers.reduce(1, (a, b) -> a * b);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        long sum = 0;
        var lines = Arrays.stream(input.split("\n")).map(String::toCharArray).toArray(char[][]::new);
        long[] numbers = new long[] { 0, 0, 0, 0, 0 };
        byte i = 0;

        for (var x = lines[0].length - 1; x >= 0; x--, i++) {
            for (var y = 0; y < lines.length - 1; y++) {
                if (lines[y][x] != ' ') {
                    numbers[i] = numbers[i] * 10 + (lines[y][x] - '0');
                }
            }

            char operation = lines[lines.length - 1][x];

            if (operation == '+' || operation == '*') {
                var ns = Arrays.stream(numbers).filter(n -> n != 0);
                sum += operation == '+' ? ns.sum() : ns.reduce(1, (a, b) -> a * b);
                numbers = new long[] { 0, 0, 0, 0, 0 };
                i = -1;
                x--;
            }
        }

        return String.valueOf(sum);
    }
}

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
        int[] columnsSize = getColumnSize(lines[lines.length - 1]);
        var consumedPosition = 0;

        for (int columnSize: columnsSize) {
            var numbers = new long[columnSize];
            char columnOperation = ' ';

            for (var j = columnSize - 1; j >= 0; j--) {
                var charPosition = consumedPosition + j;
                long n = 0;

                for (var i = 0; i < lines.length - 1; i++) {
                    if (lines[i][charPosition] != ' ') {
                        n = n * 10 + (lines[i][charPosition] - '0');
                    }
                }

                numbers[j] = n;
            }

            sum += lines[lines.length - 1][consumedPosition] == '+'
                ? Arrays.stream(numbers).sum()
                : Arrays.stream(numbers).reduce(1, (a, b) -> a * b);
            consumedPosition += columnSize + 1;
        }

        return String.valueOf(sum);
    }

    private int[] getColumnSize(char[] operationsLine) {
        int[] columnSize = new int[1500];
        int totalNumbers = 0;
        byte tempColumnSize = 0;

        for (char c : operationsLine) {
            if (c == '*' || c == '+') {
                if (totalNumbers >= 1) {
                    columnSize[totalNumbers - 1] = tempColumnSize - 1;
                }

                totalNumbers++;
                tempColumnSize = 0;
            }

            tempColumnSize++;
        }

        columnSize[totalNumbers - 1] = tempColumnSize;

        return Arrays.copyOf(columnSize, totalNumbers);
    }
}

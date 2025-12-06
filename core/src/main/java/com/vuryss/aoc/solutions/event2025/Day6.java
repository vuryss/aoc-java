package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
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

        for (var line: input.trim().split("\n")) {
            if (line.contains("*")) {
                var operations = line.split("\\s+");

                for (var i = 0; i < operations.length; i++) {
                    var operationNumbers = lists.get(i);
                    var operation = operations[i];

                    if (operation.equals("*")) {
                        long result = 1;

                        for (var number: operationNumbers) {
                            result *= number;
                        }

                        sum += result;
                        operationNumbers.clear();
                    } else if (operation.equals("+")) {
                        long result = 0;

                        for (var number: operationNumbers) {
                            result += number;
                        }

                        sum += result;
                        operationNumbers.clear();
                    }
                }
            }

            var numbers = StringUtil.ints(line);

            for (var i = 0; i < numbers.size(); i++) {
                if (i >= lists.size()) {
                    lists.add(new ArrayList<>());
                }
                lists.get(i).add(numbers.get(i));
            }
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
            var numbers = new ArrayList<Integer>();
            char columnOperation = ' ';

            for (var j = columnSize - 1; j >= 0; j--) {
                var charPosition = consumedPosition + j;
                var n = new StringBuilder();

                for (var line : lines) {
                    if (line[consumedPosition] == '*' || line[consumedPosition] == '+') {
                        columnOperation = line[consumedPosition];
                        break;
                    }

                    if (line.length <= charPosition) {
                        continue;
                    }

                    if (line[charPosition] == ' ') {
                        continue;
                    }

                    n.append(line[charPosition]);
                }

                if (!n.toString().isEmpty()) {
                    numbers.add(Integer.parseInt(n.toString()));
                }
            }

            consumedPosition += columnSize + 1;

            if (columnOperation == '+') {
                sum += numbers.stream().mapToLong(Integer::intValue).sum();
            } else {
                sum += numbers.stream().mapToLong(Integer::intValue).reduce(1, (a, b) -> a * b);
            }
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

package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(".^^.^.^^^^", "38");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var rows = isTest ? 10 : 40;
        return String.valueOf(solveWithRows(input.trim(), rows));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return String.valueOf(solveWithRows(input.trim(), 400_000));
    }

    private long solveWithRows(String input, int totalRows) {
        var width = input.length();
        var currentRow = input.toCharArray();
        var nextRow = new char[width];
        char[] temp;
        long safeTiles = countSafeTiles(currentRow);

        for (var i = 1; i < totalRows; i++) {
            generateNextRow(currentRow, nextRow);
            safeTiles += countSafeTiles(nextRow);

            temp = currentRow;
            currentRow = nextRow;
            nextRow = temp;
        }

        return safeTiles;
    }

    private void generateNextRow(char[] currentRow, char[] nextRow) {
        var width = currentRow.length;
        char left, right;

        for (var i = 0; i < width; i++) {
            left = i == 0 ? '.' : currentRow[i - 1];
            right = i == width - 1 ? '.' : currentRow[i + 1];

            nextRow[i] = (left == right) ? '.' : '^';
        }
    }

    private int countSafeTiles(char[] row) {
        var count = 0;

        for (var c : row) if (c == '.') count++;

        return count;
    }
}


package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("18", "33,45");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("18", "90,269,16", "42", "232,251,12");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        int[][] grid = new int[300][300];
        var serialNumber = Integer.parseInt(input.trim());

        for (var x = 0; x < 300; x++) {
            for (var y = 0; y < 300; y++) {
                grid[y][x] = calculatePowerLevel(x, y, serialNumber);
            }
        }

        int max = 0, maxX = 0, maxY = 0;

        for (var x = 0; x < 300 - 3; x++) {
            for (var y = 0; y < 300 - 3; y++) {
                int sum = 0;
                for (var dx = 0; dx < 3; dx++) {
                    for (var dy = 0; dy < 3; dy++) {
                        sum += grid[y + dy][x + dx];
                    }
                }

                if (sum > max) {
                    max = sum;
                    maxX = x;
                    maxY = y;
                }
            }
        }

        return String.format("%d,%d", maxX, maxY);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        int[][][] grid = new int[300][300][301];
        var serialNumber = Integer.parseInt(input.trim());

        for (var x = 0; x < 300; x++) {
            for (var y = 0; y < 300; y++) {
                grid[y][x][0] = 0;
                grid[y][x][1] = calculatePowerLevel(x, y, serialNumber);
            }
        }

        int max = 0, maxX = 0, maxY = 0, maxSize = 0;

        for (var y = 298; y >= 0; y--) {
            for (var x = 298; x >= 0; x--) {
                int maxSquare = Math.min(300 - x, 300 - y);

                for (var size = 2; size <= maxSquare; size++) {
                    grid[y][x][size] = grid[y][x][1]
                        + grid[y][x + 1][size - 1] // We add the square to the right with size -1
                        + grid[y + 1][x][size - 1] // We add the square below with size -1
                        - grid[y + 1][x + 1][size - 2]  // The above two added a middle square twice, so we subtract it
                        + grid[y + size - 1][x + size - 1][1]; // Opposite corner

                    if (grid[y][x][size] > max) {
                        max = grid[y][x][size];
                        maxX = x;
                        maxY = y;
                        maxSize = size;
                    }
                }
            }
        }

        return String.format("%d,%d,%d", maxX, maxY, maxSize);
    }

    private int calculatePowerLevel(int x, int y, int serialNumber) {
        int rackId = x + 10;
        return (rackId * y + serialNumber) * rackId / 100 % 10 - 5;
    }
}

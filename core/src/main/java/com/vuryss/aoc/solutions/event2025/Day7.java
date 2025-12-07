package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;


@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """,
            "21"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """,
            "40"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        char[][] grid = Arrays.stream(input.trim().split("\n")).map(String::toCharArray).toArray(char[][]::new);
        boolean[][] hasBeam = new boolean[grid.length][grid[0].length];
        int count = 0;

        outer:
        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    hasBeam[y][x] = true;
                    break outer;
                }
            }
        }

        for (var y = 1; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                if (hasBeam[y-1][x]) {
                    if (grid[y][x] == '^') {
                        hasBeam[y][x - 1] = true;
                        hasBeam[y][x + 1] = true;
                        count++;
                    } else {
                        hasBeam[y][x] = true;
                    }
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        char[][] grid = Arrays.stream(input.trim().split("\n")).map(String::toCharArray).toArray(char[][]::new);
        long count = 0;
        long[][] counts = new long[grid.length][grid[0].length];

        outer:
        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    counts[y][x] = 1;
                    break outer;
                }
            }
        }

        for (var y = 1; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                if (counts[y-1][x] > 0) {
                    if (grid[y][x] == '^') {
                        counts[y][x - 1] += counts[y-1][x];
                        counts[y][x + 1] += counts[y-1][x];
                    } else {
                        counts[y][x] += counts[y-1][x];
                    }
                }
            }
        }

        for (var x = 0; x < grid[grid.length - 1].length; x++) {
            count += counts[grid.length - 1][x];
        }

        return String.valueOf(count);
    }
}

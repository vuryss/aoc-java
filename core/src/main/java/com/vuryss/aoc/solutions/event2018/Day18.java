package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .#.#...|#.
            .....#|##|
            .|..|...#.
            ..|#.....#
            #.#|||#|#|
            ...#.||...
            .|....|...
            ||...#|.#|
            |.||||..|.
            ...#.|..|.
            """,
            "1147"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return calculateResourceValue(input, 10);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return calculateResourceValue(input, 1000000000);
    }

    private int gridHash(char[][] grid) {
        return Arrays.deepHashCode(grid);
    }

    private String calculateResourceValue(String input, int minutes) {
        char[][] grid = Arrays.stream(input.trim().split("\n")).map(String::toCharArray).toArray(char[][]::new);
        int size = grid.length;
        Point point;
        var seen = new HashMap<Integer, Integer>();
        seen.put(gridHash(grid), 0);
        var foundCycle = false;

        for (var i = 0; i < minutes; i++) {
            char[][] newGrid = new char[size][size];

            for (var y = 0; y < size; y++) {
                for (var x = 0; x < size; x++) {
                    int trees = 0, lumberyards = 0;

                    for (var p: new Point(x, y).surroundingPoints()) {
                        if (p.x < 0 || p.x >= size || p.y < 0 || p.y >= size) {
                            continue;
                        }

                        if (grid[p.y][p.x] == '|') {
                            trees++;
                        } else if (grid[p.y][p.x] == '#') {
                            lumberyards++;
                        }
                    }

                    if (grid[y][x] == '.') {
                        newGrid[y][x] = trees >= 3 ? '|' : '.';
                    } else if (grid[y][x] == '|') {
                        newGrid[y][x] = lumberyards >= 3 ? '#' : '|';
                    } else {
                        newGrid[y][x] = lumberyards >= 1 && trees >= 1 ? '#' : '.';
                    }
                }
            }

            grid = newGrid;

            if (foundCycle) {
                continue;
            }

            if (seen.containsKey(gridHash(grid))) {
                int cycleSize = i - seen.get(gridHash(grid));
                int minutesLeft = minutes - i;
                int leftFromLastCycle = minutesLeft % cycleSize;
                i = minutes - leftFromLastCycle;
                foundCycle = true;
            } else {
                seen.put(gridHash(grid), i);
            }
        }

        int trees = 0, lumberyards = 0;

        for (var row: grid) {
            for (var c: row) {
                if (c == '|') {
                    trees++;
                } else if (c == '#') {
                    lumberyards++;
                }
            }
        }

        return String.valueOf(trees * lumberyards);
    }
}

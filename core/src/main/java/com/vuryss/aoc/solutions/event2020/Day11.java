package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.GridUtil;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
            """,
            "37"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
            """,
            "26"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return countStableSeats(input, 4, 1) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return countStableSeats(input, 5, 1000) + "";
    }

    private int countStableSeats(String input, int tolerance, int lookDistance) {
        var grid = StringUtil.toCharGrid(input.trim());

        while (true) {
            var newGrid = new char[grid.length][grid[0].length];

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[0].length; x++) {
                    if (grid[y][x] == '.') {
                        newGrid[y][x] = grid[y][x];
                        continue;
                    }

                    int count = 0;

                    for (var delta : GridUtil.SURROUNDING_DELTAS) {
                        count += look(grid, y, x, delta[0], delta[1], lookDistance);
                    }

                    if (grid[y][x] == 'L' && count == 0) newGrid[y][x] = '#';
                    else if (grid[y][x] == '#' && count >= tolerance) newGrid[y][x] = 'L';
                    else newGrid[y][x] = grid[y][x];
                }
            }

            if (Arrays.deepEquals(newGrid, grid)) break;
            grid = newGrid;
        }

        int count = 0;

        for (char[] chars : grid) for (var ch: chars) if (ch == '#') count++;

        return count;
    }

    private int look(char[][] grid, int y, int x, int dy, int dx, int limit) {
        y += dy;
        x += dx;

        while (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length && limit-- > 0) {
            if (grid[y][x] == 'L') return 0;
            if (grid[y][x] == '#') return 1;

            y += dy;
            x += dx;
        }

        return 0;
    }
}

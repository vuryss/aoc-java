package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            x=495, y=2..7
            y=7, x=495..501
            x=501, y=3..7
            x=498, y=2..4
            x=506, y=1..2
            x=498, y=10..13
            x=504, y=10..13
            y=13, x=498..504
            """,
            "57"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            x=495, y=2..7
            y=7, x=495..501
            x=501, y=3..7
            x=498, y=2..4
            x=506, y=1..2
            x=498, y=10..13
            x=504, y=10..13
            y=13, x=498..504
            """,
            "29"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = parseGrid(input);
        simulateWater(grid);

        var count = 0;

        for (char[] chars : grid) {
            for (char c : chars) {
                if (c == '|' || c == '~') {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = parseGrid(input);
        simulateWater(grid);

        int count = 0;

        for (char[] chars : grid) {
            for (char c : chars) {
                if (c == '~') {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private void simulateWater(char[][] grid) {
        var waterSprint = new Point(500, 0);
        grid[waterSprint.y][waterSprint.x] = '|';
        var maxY = grid.length - 1;
        var queue = new ArrayDeque<Point>();
        queue.add(waterSprint);

        while (!queue.isEmpty()) {
            var point = queue.getLast();

            // Fall down if possible
            var down = point.down();

            if (down.y > maxY || grid[down.y][down.x] == '|') {
                queue.removeLast();
                continue;
            }

            if (grid[down.y][down.x] == '.') {
                grid[down.y][down.x] = '|';
                queue.addLast(down);
                continue;
            }

            // Flow left and right
            var left = point.left();
            var underLeft = left.down();
            var right = point.right();
            var underRight = right.down();

            while (grid[left.y][left.x] != '#' && grid[underLeft.y][underLeft.x] != '.') {
                grid[left.y][left.x] = '|';
                left = left.left();
                underLeft = left.down();
            }

            while (grid[right.y][right.x] != '#' && grid[underRight.y][underRight.x] != '.') {
                grid[right.y][right.x] = '|';
                right = right.right();
                underRight = right.down();
            }

            boolean leftBlocked = grid[left.y][left.x] == '#';
            boolean rightBlocked = grid[right.y][right.x] == '#';

            if (leftBlocked && rightBlocked) {
                for (var p = left.right(); !p.equals(right); p = p.right()) {
                    grid[p.y][p.x] = '~';
                }
            }

            queue.removeLast();

            if (!leftBlocked) {
                grid[left.y][left.x] = '|';
                queue.addLast(left);
            }

            if (!rightBlocked) {
                grid[right.y][right.x] = '|';
                queue.addLast(right);
            }
        }
    }

    private char[][] parseGrid(String input) {
        var grid = new char[2000][2000];
        var minY = Integer.MAX_VALUE;
        var maxY = 0;

        for (var y = 0; y < 2000; y++) {
            Arrays.fill(grid[y], '.');
        }

        for (var lines: input.trim().split("\n")) {
            var parts = StringUtil.ints(lines);
            boolean isX = lines.startsWith("x");
            int j = parts.get(0);

            if (isX) {
                maxY = Math.max(maxY, parts.get(2));
                minY = Math.min(minY, parts.get(1));
            } else {
                if (j > maxY) maxY = j;
                if (j < minY) minY = j;
            }

            for (var i = parts.get(1); i <= parts.get(2); i++) {
                grid[isX ? i : j][isX ? j : i] = '#';
            }
        }

        return Arrays.copyOfRange(grid, minY, maxY + 1);
    }
}

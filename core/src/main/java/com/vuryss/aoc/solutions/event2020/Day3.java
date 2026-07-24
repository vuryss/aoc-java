package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
            """,
            "7"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
            """,
            "336"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = StringUtil.toCharGrid(input.trim());

        return countSlope(grid, new Point(3, 1)) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = StringUtil.toCharGrid(input.trim());
        var slopes = List.of(new Point(1, 1), new Point(3, 1), new Point(5, 1), new Point(7, 1), new Point(1, 2));
        var product = 1L;

        for (var slope: slopes) product *= countSlope(grid, slope);

        return product + "";
    }

    public int countSlope(char[][] grid, Point slope) {
        var count = 0;
        var point = new Point(0, 0);
        var maxX = grid[0].length;

        do {
            count += grid[point.y][point.x % maxX] == '#' ? 1 : 0;
            point.x += slope.x;
            point.y += slope.y;
        } while (point.y < grid.length);

        return count;
    }
}

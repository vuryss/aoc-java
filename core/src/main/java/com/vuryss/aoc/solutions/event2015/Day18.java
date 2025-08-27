package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Util;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .#.#.#
            ...##.
            #....#
            ..#...
            #.#..#
            ####..
            """,
            "4"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            .#.#.#
            ...##.
            #....#
            ..#...
            #.#..#
            ####..
            """,
            "17"
        );
    }

    @Override
    public String part1Solution(String input) {
        var grid = Util.inputToGrid(input.trim());
        var steps = grid.size() == 10000 ? 100 : 4;

        grid = simulateGameOfLife(grid, steps, false);

        return String.valueOf(grid.values().stream().filter(c -> c == '#').count());
    }

    @Override
    public String part2Solution(String input) {
        var grid = Util.inputToGrid(input.trim());
        var steps = grid.size() == 10000 ? 100 : 5;

        grid = simulateGameOfLife(grid, steps, true);

        return String.valueOf(grid.values().stream().filter(c -> c == '#').count());
    }

    private HashMap<Point, Character> simulateGameOfLife(
        HashMap<Point, Character> grid,
        int steps,
        boolean stickyCorners
    ) {
        var size = (int) Math.sqrt(grid.size());

        if (stickyCorners) {
            turnOnCorners(grid, size);
        }

        for (var step = 0; step < steps; step++) {
            var newGrid = new HashMap<Point, Character>();

            for (var entry: grid.entrySet()) {
                var point = entry.getKey();
                var value = entry.getValue();
                var count = 0;

                for (var p: point.surroundingPoints()) {
                    if (grid.getOrDefault(p, '.') == '#') {
                        count++;
                    }
                }

                if (value == '#') {
                    newGrid.put(point, count == 2 || count == 3 ? '#' : '.');
                } else {
                    newGrid.put(point, count == 3 ? '#' : '.');
                }
            }

            grid = newGrid;

            if (stickyCorners) {
                turnOnCorners(grid, size);
            }
        }

        return grid;
    }

    private void turnOnCorners(HashMap<Point, Character> grid, int size) {
        grid.put(new Point(0, 0), '#');
        grid.put(new Point(0, size - 1), '#');
        grid.put(new Point(size - 1, 0), '#');
        grid.put(new Point(size - 1, size - 1), '#');
    }
}

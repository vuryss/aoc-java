package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Day12 implements DayInterface {
    private final List<Point> deltas = List.of(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0));
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
        """
        Sabqponm
        abcryxxl
        accszExk
        acctuvwj
        abdefghi
        """,
        "31"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
            """,
            "29"
        );
    }

    @Override
    public String part1Solution(String input) {
        return findShortestPath(input, true);
    }

    @Override
    public String part2Solution(String input) {
        return findShortestPath(input, false);
    }

    private String findShortestPath(String input, boolean fromPredefinedStartPosition) {
        var lines = input.split("\n");
        char[][] grid = new char[lines.length][lines[0].length()];
        var visited = new HashSet<Point>();

        Point startPosition = null, targetPosition = null;

        for (var rowId = 0; rowId < lines.length; rowId++) {
            for (var columnId = 0; columnId < lines[0].length(); columnId++) {
                grid[rowId][columnId] = lines[rowId].charAt(columnId);

                if (grid[rowId][columnId] == 'S') {
                    startPosition = new Point(columnId, rowId);
                    grid[rowId][columnId] = 'a';
                }

                if (grid[rowId][columnId] == 'E') {
                    targetPosition = new Point(columnId, rowId);
                    grid[rowId][columnId] = 'z';
                }
            }
        }

        var queue = new PriorityQueue<PathState>(Comparator.comparingInt(a -> a.steps));
        queue.add(new PathState(targetPosition, 0));

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (visited.contains(state.position)) {
                continue;
            }

            var completed = fromPredefinedStartPosition
                ? state.position.equals(startPosition)
                : grid[state.position.y][state.position.x] == 'a';

            if (completed) {
                return String.valueOf(state.steps);
            }

            visited.add(state.position);

            for (var delta: deltas) {
                var nextPosition = new Point(state.position.x + delta.x, state.position.y + delta.y);

                if (nextPosition.x < 0
                    || nextPosition.x >= lines[0].length()
                    || nextPosition.y < 0
                    || nextPosition.y >= lines.length
                    || visited.contains(nextPosition)
                    || grid[nextPosition.y][nextPosition.x] < grid[state.position.y][state.position.x] - 1
                ) {
                    continue;
                }

                queue.add(new PathState(nextPosition, state.steps + 1));
            }
        }

        return "";
    }

    record PathState(Point position, int steps) {}
}

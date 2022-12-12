package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        int[][] gridMinSteps = new int[grid.length][grid[0].length];

        Point startPosition = null, targetPosition = null;

        for (var rowId = 0; rowId < lines.length; rowId++) {
            for (var columnId = 0; columnId < lines[0].length(); columnId++) {
                grid[rowId][columnId] = lines[rowId].charAt(columnId);
                gridMinSteps[rowId][columnId] = Integer.MAX_VALUE;

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
        queue.add(new PathState(targetPosition, 0, new HashSet<>()));

        while (!queue.isEmpty()) {
            var state = queue.poll();
            var completed = fromPredefinedStartPosition
                ? state.position.equals(startPosition)
                : grid[state.position.y][state.position.x] == 'a';

            if (completed) {
                return String.valueOf(state.steps);
            }

            for (var delta: deltas) {
                var nextPosition = new Point(state.position.x + delta.x, state.position.y + delta.y);

                if (nextPosition.x < 0
                    || nextPosition.x >= lines[0].length()
                    || nextPosition.y < 0
                    || nextPosition.y >= lines.length
                    || state.previousPositions.contains(nextPosition)
                    || grid[nextPosition.y][nextPosition.x] < grid[state.position.y][state.position.x] - 1
                    || gridMinSteps[nextPosition.y][nextPosition.x] <= state.steps + 1
                ) {
                    continue;
                }

                var newState = new PathState(nextPosition, state.steps + 1, new HashSet<>(state.previousPositions));
                newState.previousPositions.add(state.position);

                gridMinSteps[nextPosition.y][nextPosition.x] = newState.steps;

                queue.add(newState);
            }
        }

        return "";
    }

    record PathState(Point position, int steps, Set<Point> previousPositions) {}
}

package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
    private int favoriteNumber;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of("10", "11");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        favoriteNumber = Integer.parseInt(input.trim());
        var position = new Point(1, 1);
        var targetPosition = isTest ? new Point(7, 4) : new Point(31, 39);
        var visited = new HashSet<Point>();
        var queue = new LinkedList<State>() {{ add(new State(position, 0)); }};

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (visited.contains(state.position)) {
                continue;
            }

            visited.add(state.position);

            if (state.position.equals(targetPosition)) {
                return String.valueOf(state.steps);
            }

            for (var adjacentPoint: state.position.getAdjacentPoints()) {
                if (isOpen(adjacentPoint.x, adjacentPoint.y) && !visited.contains(adjacentPoint)) {
                    queue.add(new State(adjacentPoint, state.steps + 1));
                }
            }
        }

        return "-not found-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        favoriteNumber = Integer.parseInt(input.trim());
        var position = new Point(1, 1);
        var visited = new HashSet<Point>();
        var queue = new LinkedList<State>() {{ add(new State(position, 0)); }};

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (visited.contains(state.position)) {
                continue;
            }

            visited.add(state.position);

            if (state.steps == 50) {
                continue;
            }

            for (var adjacentPoint: state.position.getAdjacentPoints()) {
                if (isOpen(adjacentPoint.x, adjacentPoint.y) && !visited.contains(adjacentPoint)) {
                    queue.add(new State(adjacentPoint, state.steps + 1));
                }
            }
        }

        return String.valueOf(visited.size());
    }

    private boolean isOpen(int x, int y) {
        if (x < 0 || y < 0) {
            return false;
        }

        var value = x * x + 3 * x + 2 * x * y + y + y * y + favoriteNumber;

        return Integer.bitCount(value) % 2 == 0;
    }

    private record State(Point position, int steps) {}
}

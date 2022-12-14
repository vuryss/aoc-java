package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;

import java.awt.*;
import java.util.*;

public class Day14 implements DayInterface {
    private static final Point[] deltas = new Point[]{new Point(0, 1), new Point(-1, 1), new Point(1, 1)};
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
            """,
            "24"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
            """,
            "93"
        );
    }

    @Override
    public String part1Solution(String input) {
        var grid = createCave(input);
        var maxY = grid.stream().mapToInt(a -> a.y).max().getAsInt();
        var totalSands = 0;

        outer:
        while (true) {
            var sandPoint = new Point(500, 0);
            var moved = false;

            do {
                moved = false;

                for (var delta : deltas) {
                    if (!grid.contains(new Point(sandPoint.x + delta.x, sandPoint.y + delta.y))) {
                        sandPoint.x += delta.x;
                        sandPoint.y += delta.y;
                        moved = true;
                        break;
                    }
                }

                if (sandPoint.y > maxY) {
                    break outer;
                }
            } while (moved);

            grid.add(sandPoint);
            totalSands++;
        }

        return String.valueOf(totalSands);
    }

    @Override
    public String part2Solution(String input) {
        var grid = createCave(input);
        var maxY = grid.stream().mapToInt(a -> a.y).max().getAsInt() + 1;
        var startPoint = new Point(500, 0);
        var visited = new HashSet<Point>();
        var queue = new LinkedList<Point>();
        queue.add(startPoint);

        while (!queue.isEmpty()) {
            var point = queue.poll();

            if (visited.contains(point)) {
                continue;
            }

            visited.add(point);

            for (var delta : deltas) {
                var nextPoint = new Point(point.x + delta.x, point.y + delta.y);
                if (!grid.contains(nextPoint) && !visited.contains(nextPoint) && nextPoint.y <= maxY) {
                    queue.add(nextPoint);
                }
            }
        }

        return String.valueOf(visited.size());
    }

    private HashSet<Point> createCave(String input) {
        var grid = new HashSet<Point>();

        for (var rockInput: input.split("\n")) {
            var points = new ArrayList<Point>();

            for (var pointInput: rockInput.trim().split(" -> ")) {
                var values = pointInput.split(",");

                points.add(new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
            }

            for (var i = 0; i < points.size() - 1; i++) {
                var from = points.get(i);
                var to = points.get(i+1);

                for (var x = Math.min(from.x, to.x); x <= Math.max(from.x, to.x); x++) {
                    for (var y = Math.min(from.y, to.y); y <= Math.max(from.y, to.y); y++) {
                        grid.add(new Point(x, y));
                    }
                }
            }
        }

        return grid;
    }
}

package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.GridUtil;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1, 1
            1, 6
            8, 3
            3, 4
            5, 5
            8, 9
            """,
            "17"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1, 1
            1, 6
            8, 3
            3, 4
            5, 5
            8, 9
            """,
            "16"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var points = parsePoints(input);
        var boundingBox = GridUtil.getBoundingBox(points);
        var infiniteLocations = new HashSet<Point>();

        // Check the perimeter from the min and max points, minus 1 to detect all the infinite locations
        for (var point: GridUtil.rectanglePerimeter(boundingBox)) {
            var closest = findClosest(point, points);

            if (closest != null) {
                infiniteLocations.add(closest);
            }
        }

        int max = 0;

        // Next for the rest, do a BFS from each point that is not infinite and see how many points are closest to it
        for (var point: points) {
            if (infiniteLocations.contains(point)) {
                continue;
            }

            var queue = new ArrayDeque<Point>() {{ add(point); }};
            var visited = new HashSet<Point>() {{ add(point); }};

            while (!queue.isEmpty()) {
                var p = queue.poll();

                for (var adjacentPoint: p.getAdjacentPoints()) {
                    if (visited.contains(adjacentPoint)) {
                        continue;
                    }

                    var closest = findClosest(adjacentPoint, points);

                    if (closest != null && closest.equals(point)) {
                        visited.add(adjacentPoint);
                        queue.add(adjacentPoint);
                    }
                }
            }

            max = Math.max(max, visited.size());
        }

        return String.valueOf(max);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var points = parsePoints(input);
        var boundingBox = GridUtil.getBoundingBox(points);
        var count = 0;
        int maxDistance = isTest ? 32 : 10000;

        for (var x = boundingBox.minX; x <= boundingBox.maxX; x++) {
            for (var y = boundingBox.minY; y <= boundingBox.maxY; y++) {
                var point = new Point(x, y);
                var totalDistance = 0;

                for (var p: points) {
                    totalDistance += point.manhattan(p);

                    if (totalDistance >= maxDistance) {
                        break;
                    }
                }

                if (totalDistance < maxDistance) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private Point findClosest(Point point, Point[] points) {
        Point closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (var p: points) {
            var distance = point.manhattan(p);

            if (distance < minDistance) {
                closest = p;
                minDistance = distance;
            } else if (distance == minDistance) {
                closest = null;
            }
        }

        return closest;
    }

    private Point[] parsePoints(String input) {
        var lines = input.trim().split("\n");
        var points = new Point[lines.length];

        for (var i = 0; i < lines.length; i++) {
            var numbers = StringUtil.ints(lines[i]);
            points[i] = new Point(numbers.get(0), numbers.get(1));
        }

        return points;
    }
}

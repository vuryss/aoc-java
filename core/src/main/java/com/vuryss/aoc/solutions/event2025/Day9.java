package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """,
            "50"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """,
            "24"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var points = new ArrayList<Point>();
        long maxArea = 0;

        for (var line: lines) {
            var coordinates = StringUtil.ints(line);
            points.add(new Point(coordinates.get(0), coordinates.get(1)));
        }

        for (var point: points) {
            for (var otherPoint: points) {
                if (point.equals(otherPoint)) {
                    continue;
                }

                long width = Math.abs(point.x - otherPoint.x) + 1;
                long height = Math.abs(point.y - otherPoint.y) + 1;
                long area = width * height;

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return String.valueOf(maxArea);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var points = new ArrayList<Point>();
        long maxArea = 0;
        var polygon = new Polygon();

        for (var line: lines) {
            var coordinates = StringUtil.ints(line);
            points.add(new Point(coordinates.get(0), coordinates.get(1)));
            polygon.addPoint(coordinates.get(0), coordinates.get(1));
        }

        var linesPoints = new ArrayList<Pair<Point, Point>>();
        var columnPoints = new ArrayList<Pair<Point, Point>>();

        points.sort(Comparator.comparingInt(point -> point.x));

        for (var i = 0; i < points.size() - 1; i += 2) {
            columnPoints.add(Pair.of(points.get(i), points.get(i + 1)));
        }

        points.sort(Comparator.comparingInt(point -> point.y));

        for (var i = 0; i < points.size() - 1; i += 2) {
            linesPoints.add(Pair.of(points.get(i), points.get(i + 1)));
        }

        for (var point: points) {
            nextPoint:
            for (var otherPoint: points) {
                if (point.equals(otherPoint)) {
                    continue;
                }

                long minX = Math.min(point.x, otherPoint.x);
                long maxX = Math.max(point.x, otherPoint.x);
                long minY = Math.min(point.y, otherPoint.y);
                long maxY = Math.max(point.y, otherPoint.y);

                for (var line: linesPoints) {
                    Point start = line.getLeft();
                    Point end = line.getRight();

                    if (start.y <= minY || start.y >= maxY) {
                        continue;
                    }

                    int from = Math.min(start.x, end.x);
                    int to = Math.max(start.x, end.x);

                    for (var x = from + 1; x < to; x++) {
                        if (x > minX && x < maxX) {
                            continue nextPoint;
                        }
                    }
                }

                for (var column: columnPoints) {
                    Point start = column.getLeft();
                    Point end = column.getRight();

                    if (start.x <= minX || start.x >= maxX) {
                        continue;
                    }

                    int from = Math.min(start.y, end.y);
                    int to = Math.max(start.y, end.y);

                    for (var y = from + 1; y < to; y++) {
                        if (y > minY && y < maxY) {
                            continue nextPoint;
                        }
                    }
                }

                long width = Math.abs(point.x - otherPoint.x) + 1;
                long height = Math.abs(point.y - otherPoint.y) + 1;

                long area = width * height;

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return String.valueOf(maxArea);
    }
}

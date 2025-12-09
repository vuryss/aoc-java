package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

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

        for (var line: lines) {
            var coordinates = StringUtil.ints(line);
            points.add(new Point(coordinates.get(0), coordinates.get(1)));
        }

        var allLines = new ArrayList<Line>();
        var allColumns = new ArrayList<Column>();

        points.sort(Comparator.<Point>comparingInt(point -> point.x).thenComparingInt(point -> point.y));

        for (var i = 0; i < points.size() - 1; i += 2) {
            allColumns.add(new Column(points.get(i).x, points.get(i).y, points.get(i + 1).y));
        }

        points.sort(Comparator.<Point>comparingInt(point -> point.y).thenComparingInt(point -> point.x));

        for (var i = 0; i < points.size() - 1; i += 2) {
            allLines.add(new Line(points.get(i).y, points.get(i).x, points.get(i + 1).x));
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

                for (var line: allLines) {
                    if (line.y <= minY || line.y >= maxY) {
                        continue;
                    }

                    if (
                        (minX < line.from && maxX > line.from || minX < line.to && maxX > line.to) // Point inside the rectangle
                        || (minX >= line.from && maxX <= line.to) // Rectangle inside the point
                    ) {
                        continue nextPoint;
                    }
                }

                for (var column: allColumns) {
                    if (column.x <= minX || column.x >= maxX) {
                        continue;
                    }

                    if (
                        (minY < column.from && maxY > column.from || minY < column.to && maxY > column.to) // Point inside the rectangle
                        || (minY >= column.from && maxY <= column.to) // Rectangle inside the point
                    ) {
                        continue nextPoint;
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

    private record Line(int y, int from, int to) {}
    private record Column(int x, int from, int to) {}
}

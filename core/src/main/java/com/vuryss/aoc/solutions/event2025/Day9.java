package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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
        var points = parsePoints(input);
        long maxArea = 0;

        for (var i = 0; i < points.length - 1; i++) {
            for (var j = i + 1; j < points.length; j++) {
                long minX = Math.min(points[i].x, points[j].x);
                long maxX = Math.max(points[i].x, points[j].x);
                long minY = Math.min(points[i].y, points[j].y);
                long maxY = Math.max(points[i].y, points[j].y);
                long area = (maxX - minX + 1) * (maxY - minY + 1);

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return String.valueOf(maxArea);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var points = parsePoints(input);
        long maxArea = 0;
        var lines = new Point[points.length][2];

        for (var i = 0; i < points.length; i++) {
            lines[i][0] = points[i];
            lines[i][1] = points[(i+1)%points.length];
        }

        for (var i = 0; i < points.length - 1; i++) {
            nextCombination:
            for (var j = i + 1; j < points.length; j++) {
                long minX = Math.min(points[i].x, points[j].x);
                long maxX = Math.max(points[i].x, points[j].x);
                long minY = Math.min(points[i].y, points[j].y);
                long maxY = Math.max(points[i].y, points[j].y);
                long area = (maxX - minX + 1) * (maxY - minY + 1);

                if (area > maxArea) {
                    for (var line: lines) {
                        long lineMinX = Math.min(line[0].x, line[1].x);
                        long lineMaxX = Math.max(line[0].x, line[1].x);
                        long lineMinY = Math.min(line[0].y, line[1].y);
                        long lineMaxY = Math.max(line[0].y, line[1].y);

                        if (lineMinX < maxX && lineMaxX > minX && lineMinY < maxY && lineMaxY > minY) {
                            continue nextCombination;
                        }
                    }

                    maxArea = area;
                }
            }
        }

        return String.valueOf(maxArea);
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

package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.*;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var points = new Point[lines.length];
        var velocities = new Point[lines.length];

        for (var i = 0; i < lines.length; i++) {
            var numbers = StringUtil.sints(lines[i]);
            points[i] = new Point(numbers.get(0), numbers.get(1));
            velocities[i] = new Point(numbers.get(2), numbers.get(3));
        }

        int maxHeightDifference;

        do {
            for (var i = 0; i < points.length; i++) {
                points[i] = points[i].add(velocities[i]);
            }

            maxHeightDifference = maxHeightDifference(points);
        } while (maxHeightDifference > 10);

        var boundingBox = GridUtil.getBoundingBox(points);
        var pointsSet = new HashSet<Point>() {{ addAll(Arrays.asList(points)); }};
        var ocrPoints = new HashSet<Point>();

        for (var y = boundingBox.minY; y <= boundingBox.maxY; y++) {
            for (var x = boundingBox.minX; x <= boundingBox.maxX; x++) {
                if (pointsSet.contains(new Point(x, y))) {
                    ocrPoints.add(new Point(x - boundingBox.minX, y - boundingBox.minY));
                }
            }
        }

        return LetterOCR.decode610(ocrPoints, 2);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var points = new Point[lines.length];
        var velocities = new Point[lines.length];

        for (var i = 0; i < lines.length; i++) {
            var numbers = StringUtil.sints(lines[i]);
            points[i] = new Point(numbers.get(0), numbers.get(1));
            velocities[i] = new Point(numbers.get(2), numbers.get(3));
        }

        int maxHeightDifference, seconds = 0;

        do {
            for (var i = 0; i < points.length; i++) {
                points[i] = points[i].add(velocities[i]);
            }

            maxHeightDifference = maxHeightDifference(points);
            seconds++;
        } while (maxHeightDifference > 10);

        return String.valueOf(seconds);
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

    private int maxHeightDifference(Point[] points) {
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (var point: points) {
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }

        return Math.abs(maxY - minY);
    }
}

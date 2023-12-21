package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Point;

import java.util.*;

@SuppressWarnings("unused")
public class Day21 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var obstacles = new HashSet<Point>();
        var start = new Point(0, 0);

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (var x = 0; x < line.length(); x++) {
                var c = line.charAt(x);

                if (c == '#') {
                    obstacles.add(new Point(x, y));
                } else if (c == 'S') {
                    start = new Point(x, y);
                }
            }
        }

        var maxX = lines[0].length() - 1;
        var maxY = lines.length - 1;

        var currentPositions = new HashSet<Point>();
        currentPositions.add(start);

        var nextPositions = new HashSet<Point>();

        for (var i = 0; i < 64; i++) {
            for (var point: currentPositions) {
                for (var nextPoint: point.getAdjacentPoints()) {
                    if (nextPoint.x < 0 || nextPoint.x > maxX || nextPoint.y < 0 || nextPoint.y > maxY) {
                        continue;
                    }

                    if (obstacles.contains(nextPoint)) {
                        continue;
                    }

                    nextPositions.add(nextPoint);
                }
            }

            currentPositions = nextPositions;
            nextPositions = new HashSet<>();
        }

        return String.valueOf(currentPositions.size());
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var obstacles = new HashSet<Point>();
        var start = new Point(0, 0);

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (var x = 0; x < line.length(); x++) {
                var c = line.charAt(x);

                if (c == '#') {
                    obstacles.add(new Point(x, y));
                } else if (c == 'S') {
                    start = new Point(x, y);
                }
            }
        }

        var maxX = lines[0].length();
        var maxY = lines.length;

        var currentPositions = new HashSet<Point>();
        currentPositions.add(start);

        var nextPositions = new HashSet<Point>();
        var lastCycleCount = 0L;
        var lastCountIncrease = 0L;
        var lastDiffIncreasePerCycle = 0L;
        var step = 0;

        for (var i = 1; i <= 26501365; i++) {
            for (var point: currentPositions) {
                for (var nextPoint: point.getAdjacentPoints()) {
                    var x = (((nextPoint.x % maxX) + maxX) % maxX);
                    var y = (((nextPoint.y % maxY) + maxY) % maxY);

                    if (obstacles.contains(new Point(x, y))) {
                        continue;
                    }

                    nextPositions.add(nextPoint);
                }
            }

            currentPositions = nextPositions;
            nextPositions = new HashSet<>();

            if (i % 131 == 65) {
                var countIncrease = currentPositions.size() - lastCycleCount;
                var increaseDifference = countIncrease - lastCountIncrease;
                var increaseDifferenceDifference = increaseDifference - lastDiffIncreasePerCycle;

                lastCycleCount = currentPositions.size();
                lastCountIncrease = countIncrease;
                lastDiffIncreasePerCycle = increaseDifference;

                if (increaseDifferenceDifference == 0) {
                    step = i;
                    break;
                }
            }
        }

        while (step < 26501365) {
            lastCountIncrease += lastDiffIncreasePerCycle;
            lastCycleCount += lastCountIncrease;
            step += 131;
        }

        return String.valueOf(lastCycleCount);
    }
}
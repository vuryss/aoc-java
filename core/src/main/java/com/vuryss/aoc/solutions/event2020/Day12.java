package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            F10
            N3
            F7
            R90
            F11
            """,
            "25"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            F10
            N3
            F7
            R90
            F11
            """,
            "286"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        Point p = new Point(0, 0);
        var dir = CompassDirection.E;

        for (var line : input.trim().split("\n")) {
            var c = line.charAt(0);
            var n = Integer.parseInt(line.substring(1));

            switch (c) {
                case 'N' -> p = p.north(n);
                case 'S' -> p = p.south(n);
                case 'E' -> p = p.east(n);
                case 'W' -> p = p.west(n);
                case 'F' -> p = p.goInDirection(dir, n);
                case 'R' -> {
                    for (var i = 0; i < n/90; i++) dir = dir.turnRight90();
                }
                case 'L' -> {
                    for (var i = 0; i < n / 90; i++) dir = dir.turnLeft90();
                }
            }
        }

        return p.manhattan(new Point(0, 0)) + "";
    }

    @SuppressWarnings({"SuspiciousNameCombination"})
    @Override
    public String part2Solution(String input, boolean isTest) {
        Point ship = new Point(0, 0);
        Point waypoint = new Point(10, -1);

        for (var line : input.trim().split("\n")) {
            var c = line.charAt(0);
            var n = Integer.parseInt(line.substring(1));

            switch (c) {
                case 'N' -> waypoint = waypoint.north(n);
                case 'S' -> waypoint = waypoint.south(n);
                case 'E' -> waypoint = waypoint.east(n);
                case 'W' -> waypoint = waypoint.west(n);
                case 'F' -> ship = ship.south(n * waypoint.y).east(n * waypoint.x);
                case 'R' -> {
                    for (var i = 0; i < n/90; i++) waypoint = new Point(-waypoint.y, waypoint.x);
                }
                case 'L' -> {
                    for (var i = 0; i < n / 90; i++) waypoint = new Point(waypoint.y, -waypoint.x);
                }
            }
        }

        return ship.manhattan(new Point(0, 0)) + "";
    }
}

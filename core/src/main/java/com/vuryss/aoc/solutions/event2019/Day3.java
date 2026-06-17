package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            R8,U5,L5,D3
            U7,R6,D4,L4
            """,
            "6",
            """
            R75,D30,R83,U83,L12,D49,R71,U7,L72
            U62,R66,U55,R34,D71,R55,D58,R83
            """,
            "159",
            """
            R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
            U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
            """,
            "135"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            R8,U5,L5,D3
            U7,R6,D4,L4
            """,
            "30",
            """
            R75,D30,R83,U83,L12,D49,R71,U7,L72
            U62,R66,U55,R34,D71,R55,D58,R83
            """,
            "610",
            """
            R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
            U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
            """,
            "410"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var steps1 = lines[0].split(",");
        var steps2 = lines[1].split(",");
        var visited = new HashSet<Point>();
        var point = new Point(0, 0);
        var minManhattanDistance = Integer.MAX_VALUE;

        for (var step : steps1) {
            var dir = Direction.fromChar(step.charAt(0));
            var steps = Integer.parseInt(step.substring(1));

            for (var i = 0; i < steps; i++) {
                point = point.goInDirection(dir);
                visited.add(point);
            }
        }

        point = new Point(0, 0);

        for (var step : steps2) {
            var dir = Direction.fromChar(step.charAt(0));
            var steps = Integer.parseInt(step.substring(1));

            for (var i = 0; i < steps; i++) {
                point = point.goInDirection(dir);
                if (visited.contains(point)) {
                    minManhattanDistance = Math.min(minManhattanDistance, point.manhattan(new Point(0, 0)));
                }
            }
        }

        return minManhattanDistance + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var steps1 = lines[0].split(",");
        var steps2 = lines[1].split(",");
        var visited = new HashMap<Point, Integer>();
        var point = new Point(0, 0);
        var count = 0;
        var minSteps = Integer.MAX_VALUE;

        for (var step : steps1) {
            var dir = Direction.fromChar(step.charAt(0));
            var steps = Integer.parseInt(step.substring(1));

            for (var i = 0; i < steps; i++) {
                point = point.goInDirection(dir);
                visited.putIfAbsent(point, ++count);
            }
        }

        point = new Point(0, 0);
        count = 0;

        for (var step : steps2) {
            var dir = Direction.fromChar(step.charAt(0));
            var steps = Integer.parseInt(step.substring(1));

            for (var i = 0; i < steps; i++) {
                point = point.goInDirection(dir);
                count++;

                if (visited.containsKey(point)) {
                    minSteps = Math.min(minSteps, count + visited.get(point));
                }
            }
        }

        return minSteps + "";
    }
}

package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Point;

import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "R2, L3", "5",
            "R2, R2, R2", "2",
            "R5, L5, R5, R3", "12"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "R8, R4, R4, R8", "4"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var sequence = input.trim().split(",\\s?");
        var point = new Point(0, 0);
        var direction = CompassDirection.N;

        for (var instruction: sequence) {
            var turn = instruction.charAt(0);
            var distance = Integer.parseInt(instruction.substring(1));

            direction = turn == 'R' ? direction.turnRight90() : direction.turnLeft90();
            point = point.goInDirection(direction, distance);
        }

        return String.valueOf(point.manhattan(new Point(0, 0)));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var visitedLocations = new HashSet<Point>();
        var sequence = input.trim().split(",\\s?");
        var point = new Point(0, 0);
        var direction = CompassDirection.N;

        for (var instruction: sequence) {
            var turn = instruction.charAt(0);
            var distance = Integer.parseInt(instruction.substring(1));

            direction = turn == 'R' ? direction.turnRight90() : direction.turnLeft90();

            for (var i = 0; i < distance; i++) {
                point = point.goInDirection(direction);

                if (!visitedLocations.add(point)) {
                    return String.valueOf(point.manhattan(new Point(0, 0)));
                }
            }
        }

        return "-not found-";
    }
}

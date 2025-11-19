package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "1", "0",
            "12", "3",
            "23", "2",
            "1024", "31"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var number = Integer.parseInt(input.trim());
        var circle = (int) Math.sqrt(number);

        if (circle % 2 == 0) {
            circle++;
        }

        var corner = circle * circle;

        while (corner > number) {
            corner -= (circle - 1);
        }

        var mid = corner + (circle - 1) / 2;
        var distance = Math.abs(number - mid) + (circle - 1) / 2;

        return String.valueOf(distance);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var spiral = new HashMap<Point, Integer>();
        var number = Integer.parseInt(input.trim());
        var position = new Point(0, 0);
        var direction = Direction.R;

        spiral.put(position, 1);

        position = position.right();
        spiral.put(position, 1);

        while (true) {
            var left = position.leftFromDirection(direction);

            if (spiral.containsKey(left)) {
                position = position.goInDirection(direction);
            } else {
                direction = direction.turnLeft();
                position = position.goInDirection(direction);
            }

            var value = 0;

            for (var point: position.surroundingPoints()) {
                if (spiral.containsKey(point)) {
                    value += spiral.get(point);
                }
            }

            spiral.put(position, value);

            if (value > number) {
                return String.valueOf(value);
            }
        }
    }
}

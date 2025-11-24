package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                 |         \s
                 |  +--+   \s
                 A  |  C   \s
             F---|--|-E---+\s
                 |  |  |  D\s
                 +B-+  +--+\s
            
            """,
            "ABCDEF"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                 |         \s
                 |  +--+   \s
                 A  |  C   \s
             F---|--|-E---+\s
                 |  |  |  D\s
                 +B-+  +--+\s
            
            """,
            "38"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return helpThePoorLittlePacketFindItsWay(input).letters();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return String.valueOf(helpThePoorLittlePacketFindItsWay(input).steps());
    }

    private Result helpThePoorLittlePacketFindItsWay(String input) {
        var lines = input.split("\n");
        var points = new HashMap<Point, Type>();
        Point start = null;

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == ' ') {
                    continue;
                }

                var point = new Point(x, y);

                if (y == 0) {
                    start = point;
                }

                points.put(point, switch (lines[y].charAt(x)) {
                    case '|' -> Type.VERTICAL;
                    case '-' -> Type.HORIZONTAL;
                    case '+' -> Type.CROSS;
                    default -> Type.LETTER;
                });
            }
        }

        var position = start;
        var direction = Direction.D;
        var letters = new StringBuilder();
        var queue = new LinkedList<State>();
        queue.add(new State(position, direction, 1));

        while (!queue.isEmpty()) {
            var state = queue.poll();
            var type = points.get(state.position);

            if (type == Type.LETTER) {
                letters.append(lines[state.position.y].charAt(state.position.x));
            }

            if (type != Type.CROSS) {
                var nextPosition = state.position.goInDirection(state.direction);

                if (!points.containsKey(nextPosition)) {
                    return new Result(letters.toString(), state.steps);
                }

                queue.add(new State(nextPosition, state.direction, state.steps + 1));
                continue;
            }

            var leftDirection = state.direction.turnLeft();
            var left = state.position.leftFromDirection(state.direction);
            var expectedLeft = leftDirection == Direction.L || leftDirection == Direction.R ? Type.HORIZONTAL : Type.VERTICAL;

            if (points.containsKey(left) && (points.get(left) == expectedLeft || points.get(left) == Type.LETTER)) {
                queue.add(new State(left, leftDirection, state.steps + 1));
                continue;
            }

            var rightDirection = state.direction.turnRight();
            var right = state.position.rightFromDirection(state.direction);
            var expectedRight = rightDirection == Direction.L || rightDirection == Direction.R ? Type.HORIZONTAL : Type.VERTICAL;

            if (points.containsKey(right) && (points.get(right) == expectedRight || points.get(right) == Type.LETTER)) {
                queue.add(new State(right, rightDirection, state.steps + 1));
            }
        }

        return new Result("PACKET LOST", 0);
    }

    private enum Type {
        VERTICAL, HORIZONTAL, CROSS, LETTER
    }

    private record State(Point position, Direction direction, int steps) {}
    private record Result(String letters, int steps) {}
}

package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    private final Map<Point, Character> keypad1 = Map.of(
        new Point(0, 0), '1',
        new Point(1, 0), '2',
        new Point(2, 0), '3',
        new Point(0, 1), '4',
        new Point(1, 1), '5',
        new Point(2, 1), '6',
        new Point(0, 2), '7',
        new Point(1, 2), '8',
        new Point(2, 2), '9'
    );

    private final Map<Point, Character> keypad2 = Map.ofEntries(
        Map.entry(new Point(2, 0), '1'),
        Map.entry(new Point(1, 1), '2'),
        Map.entry(new Point(2, 1), '3'),
        Map.entry(new Point(3, 1), '4'),
        Map.entry(new Point(0, 2), '5'),
        Map.entry(new Point(1, 2), '6'),
        Map.entry(new Point(2, 2), '7'),
        Map.entry(new Point(3, 2), '8'),
        Map.entry(new Point(4, 2), '9'),
        Map.entry(new Point(1, 3), 'A'),
        Map.entry(new Point(2, 3), 'B'),
        Map.entry(new Point(3, 3), 'C'),
        Map.entry(new Point(2, 4), 'D')
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                ULL
                RRDDD
                LURDL
                UUUUD
                """,
            "1985"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                ULL
                RRDDD
                LURDL
                UUUUD
                """,
            "5DB3"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, keypad1, new Point(1, 1));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, keypad2, new Point(0, 2));
    }

    public String solve(String input, Map<Point, Character> keypad, Point position) {
        var lines = input.trim().split("\n");
        var code = new StringBuilder();

        for (var line: lines) {
            for (var c: line.toCharArray()) {
                var newPosition = position.forwardFromDirection(Direction.fromChar(c));

                if (keypad.containsKey(newPosition)) {
                    position = newPosition;
                }
            }

            code.append(keypad.get(position));
        }

        return code.toString();
    }
}

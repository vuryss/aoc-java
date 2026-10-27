package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.*;

import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            rect 3x2
            rotate column x=1 by 1
            rotate row y=0 by 4
            rotate column x=1 by 1
            """,
            "6"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var screen = buildScreen(input);
        var count = 0;

        for (var y = 0; y < 6; y++) {
            for (var x = 0; x < 50; x++) {
                if (screen[x][y]) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var screen = buildScreen(input);
        var points = new HashSet<Point>();

        for (var y = 0; y < 6; y++) {
            for (var x = 0; x < 50; x++) {
                if (screen[x][y]) {
                    points.add(new Point(x, y));
                }
            }
        }

        return LetterOCR.decode46(points);
    }

    private boolean[][] buildScreen(String input) {
        var lines = input.trim().split("\n");
        var screen = new boolean[50][6];

        for (var line: lines) {
            var numbers = StringUtil.ints(line);

            if (line.startsWith("rect")) {
                for (var x = 0; x < numbers.get(0); x++) {
                    for (var y = 0; y < numbers.get(1); y++) {
                        screen[x][y] = true;
                    }
                }

                continue;
            }

            if (line.startsWith("rotate column")) {
                var newColumn = new boolean[6];

                for (var y = 0; y < 6; y++) {
                    newColumn[(y + numbers.get(1)) % 6] = screen[numbers.get(0)][y];
                }

                screen[numbers.get(0)] = newColumn;
                continue;
            }

            var newRow = new boolean[50];

            for (var x = 0; x < 50; x++) {
                newRow[(x + numbers.get(1)) % 50] = screen[x][numbers.get(0)];
            }

            for (var x = 0; x < 50; x++) {
                screen[x][numbers.get(0)] = newRow[x];
            }
        }

        return screen;
    }
}

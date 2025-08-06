package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
            """,
            "13"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
            """,
            "1",
            """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
            """,
            "36"
        );
    }

    @Override
    public String part1Solution(String input) {
        return executeForKnots(input, 2);
    }

    @Override
    public String part2Solution(String input) {
        return executeForKnots(input, 10);
    }

    private String executeForKnots(String input, int knotsCount) {
        var moves = input.split("\n");
        var knots = new Point[knotsCount];
        var tailPositions = new HashSet<Point>();
        var xDelta = Map.of("R", 1, "L", -1, "U", 0, "D", 0);
        var yDelta = Map.of("R", 0, "L", 0, "U", 1, "D", -1);
        var knotDelta = Map.of(0, 0, 1, 0, 2, 1, -1, 0, -2, -1);

        for (var i = 0; i < knotsCount; i++) {
            knots[i] = new Point(0, 0);
        }

        tailPositions.add(new Point(knots[knotsCount - 1].x, knots[knotsCount - 1].y));

        for (var move: moves) {
            var parts = move.split(" ");

            for (var i = 0; i < Integer.parseInt(parts[1]); i++) {
                knots[0].x += xDelta.get(parts[0]);
                knots[0].y += yDelta.get(parts[0]);

                for (var j = 1; j < knotsCount; j++) {
                    var knotDeltaX = knotDelta.get(knots[j-1].x - knots[j].x);
                    var knotDeltaY = knotDelta.get(knots[j-1].y - knots[j].y);

                    knots[j].x += knotDeltaX;
                    knots[j].y += knotDeltaY;

                    if (knotDeltaX != 0 && knotDeltaY == 0) {
                        knots[j].y += knots[j-1].y - knots[j].y;
                    }

                    if (knotDeltaY != 0 && knotDeltaX == 0) {
                        knots[j].x += knots[j-1].x - knots[j].x;
                    }
                }

                tailPositions.add(new Point(knots[knotsCount - 1].x, knots[knotsCount - 1].y));
            }
        }

        return String.valueOf(tailPositions.size());
    }
}

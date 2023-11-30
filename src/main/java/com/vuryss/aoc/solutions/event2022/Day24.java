package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Point;

import java.util.*;

public class Day24 implements DayInterface {
    List<Point> moves = List.of(
        new Point(0, -1),
        new Point(1, 0),
        new Point(0, 1),
        new Point(-1, 0)
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
            """,
            "18"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
            """,
            "54"
        );
    }

    @Override
    public String part1Solution(String input) {
        var blizzards = new ArrayList<Blizzard>();
        var lines = input.trim().split("\n");
        int minY = 1, maxY = lines.length - 2, minX = 1, maxX = lines[0].length() - 2;
        Point startPosition = null, targetPosition = null;

        for (int x = 0; x < lines[0].length(); x++) {
            if (lines[0].charAt(x) == '.') {
                startPosition = new Point(x, 0);
                break;
            }
        }

        for (int x = 0; x < lines[0].length(); x++) {
            if (lines[lines.length - 1].charAt(x) == '.') {
                targetPosition = new Point(x, lines.length - 1);
                break;
            }
        }

        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (int x = 0; x < line.length(); x++) {
                var c = line.charAt(x);

                if (c == '^') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.NORTH));
                } else if (c == '>') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.EAST));
                } else if (c == 'v') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.SOUTH));
                } else if (c == '<') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.WEST));
                }
            }
        }

        return String.valueOf(calculateMinutes(
            startPosition,
            targetPosition,
            blizzards,
            minX, maxX,
            minY, maxY
        ));
    }

    @Override
    public String part2Solution(String input) {
        var blizzards = new ArrayList<Blizzard>();
        var lines = input.trim().split("\n");
        int minY = 1, maxY = lines.length - 2, minX = 1, maxX = lines[0].length() - 2;
        Point startPosition = null, targetPosition = null;

        for (int x = 0; x < lines[0].length(); x++) {
            if (lines[0].charAt(x) == '.') {
                startPosition = new Point(x, 0);
                break;
            }
        }

        for (int x = 0; x < lines[0].length(); x++) {
            if (lines[lines.length - 1].charAt(x) == '.') {
                targetPosition = new Point(x, lines.length - 1);
                break;
            }
        }

        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (int x = 0; x < line.length(); x++) {
                var c = line.charAt(x);

                if (c == '^') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.NORTH));
                } else if (c == '>') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.EAST));
                } else if (c == 'v') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.SOUTH));
                } else if (c == '<') {
                    blizzards.add(new Blizzard(new Point(x, y), Blizzard.Direction.WEST));
                }
            }
        }

        var firstPassMinutes = calculateMinutes(
            startPosition,
            targetPosition,
            blizzards,
            minX, maxX,
            minY, maxY
        );

        var backwardsMinutes = calculateMinutes(
            targetPosition,
            startPosition,
            blizzards,
            minX, maxX,
            minY, maxY
        );

        var finalPassMinutes = calculateMinutes(
            startPosition,
            targetPosition,
            blizzards,
            minX, maxX,
            minY, maxY
        );

        return String.valueOf(firstPassMinutes + backwardsMinutes + finalPassMinutes);
    }

    public int calculateMinutes(
        Point startPosition,
        Point endPosition,
        List<Blizzard> blizzards,
        int minX,
        int maxX,
        int minY,
        int maxY
    ) {
        var minutes = 0;
        Set<Point> positions = Set.of(startPosition);
        var nextPositions = new HashSet<>(positions);
        int minDistanceToTarget = 10000;

        while (true) {
            minutes++;

            for (var blizzard: blizzards) {
                blizzard.move(minX, maxX, minY, maxY);
            }

            for (var position: positions) {
                movesLoop:
                for (var move: moves) {
                    var newPosition = position.add(move);

                    if (newPosition.equals(endPosition)) {
                        return minutes;
                    }

                    if (newPosition.x < minX || newPosition.x > maxX || newPosition.y < minY || newPosition.y > maxY) {
                        continue;
                    }

                    for (var blizzard: blizzards) {
                        if (blizzard.position.equals(newPosition)) {
                            continue movesLoop;
                        }
                    }

                    var distanceToTarget = Math.abs(newPosition.x - endPosition.x) + Math.abs(newPosition.y - endPosition.y);

                    if (distanceToTarget > minDistanceToTarget + 20) {
                        continue;
                    }

                    minDistanceToTarget = Math.min(minDistanceToTarget, distanceToTarget);

                    nextPositions.add(newPosition);
                }

                boolean isSafe = true;

                for (var blizzard: blizzards) {
                    if (blizzard.position.equals(position)) {
                        isSafe = false;
                        break;
                    }
                }

                if (isSafe) {
                    nextPositions.add(position);
                }
            }

            positions = nextPositions;
            nextPositions = new HashSet<>();
        }
    }

    class Blizzard {
        enum Direction {
            NORTH, EAST, SOUTH, WEST;
        }

        Point position;
        Direction direction;

        Blizzard (Point position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        void move(int minX, int maxX, int minY, int maxY) {
            switch (direction) {
                case NORTH -> {
                    if (--position.y < minY) {
                        position.y = maxY;
                    }
                }
                case EAST -> {
                    if (++position.x > maxX) {
                        position.x = minX;
                    }
                }
                case SOUTH -> {
                    if (++position.y > maxY) {
                        position.y = minY;
                    }
                }
                case WEST -> {
                    if (--position.x < minX) {
                        position.x = maxX;
                    }
                }
            }
        }
    }
}

package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("unused")
public class Day18 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """,
            "62"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            R 6 (#000020)
            D 5 (#000021)
            L 2 (#000022)
            D 2 (#000023)
            """,
            "9",
            """
            R 6 (#000060)
            D 5 (#000051)
            L 2 (#000022)
            D 2 (#000021)
            R 2 (#000020)
            D 2 (#000021)
            L 5 (#000052)
            U 2 (#000023)
            L 1 (#000012)
            U 2 (#000023)
            R 2 (#000020)
            U 3 (#000033)
            L 2 (#000022)
            U 2 (#000023)
            """,
            "62",
            """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """,
            "952408144115"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Character>();
        var position = new Point(0, 0);
        int minX = 0, maxX = 0, minY = 0, maxY = 0;

        for (var line: lines) {
            var parts = line.split(" ");
            var direction = parts[0];
            var distance = Integer.parseInt(parts[1]);
            var color = parts[2].substring(1, 8);

            switch (direction) {
                case "R" -> {
                    for (var x = 1; x <= distance; x++) {
                        position = position.right();
                        grid.put(position, '#');
                    }
                    maxX = Math.max(maxX, position.x);
                }
                case "L" -> {
                    for (var x = 1; x <= distance; x++) {
                        position = position.left();
                        grid.put(position, '#');
                    }
                    minX = Math.min(minX, position.x);
                }
                case "U" -> {
                    for (var x = 1; x <= distance; x++) {
                        position = position.up();
                        grid.put(position, '#');
                    }
                    minY = Math.min(minY, position.y);
                }
                case "D" -> {
                    for (var x = 1; x <= distance; x++) {
                        position = position.down();
                        grid.put(position, '#');
                    }
                    maxY = Math.max(maxY, position.y);
                }
            }
        }

        // Take all 4 points around the start and find which one is "inside" to start the filling
        var start = new Point(0, 0);

        for (var point: start.surroundingPoints()) {
            if (grid.containsKey(point)) {
                continue;
            }

            var countWalls = 0;

            for (var x = minX; x < point.x; x++) {
                if (grid.containsKey(new Point(x, point.y))) {
                    countWalls++;
                }
            }

            if (countWalls % 2 == 1) {
                start = point;
                break;
            }
        }

        Point finalStart = start;
        var queue = new LinkedList<Point>(){{ add(finalStart); }};
        var visited = new HashSet<Point>();

        while (!queue.isEmpty()) {
            var point = queue.removeFirst();

            if (visited.contains(point)) {
                continue;
            }

            visited.add(point);
            grid.put(point, '#');

            for (var p: point.surroundingPoints()) {
                if (grid.containsKey(p) || visited.contains(p)) {
                    continue;
                }

                queue.add(p);
            }
        }

        return String.valueOf(grid.size());
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Character>();
        var position = new Point2D.Double(0, 0);
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        var pointsList = new ArrayList<Point2D.Double>();
        var index = 0;
        var directions = new ArrayList<Direction>();

        for (var line: lines) {
            var parts = line.split(" ");
            var color = parts[2].substring(1, 8);
            var direction = switch (color.charAt(6)) {
                case '0' -> Direction.R;
                case '1' -> Direction.D;
                case '2' -> Direction.L;
                case '3' -> Direction.U;
                default -> throw new RuntimeException("Unknown direction: " + color.charAt(5));
            };

            directions.add(direction);
        }

        var xDelta = 0;
        var yDelta = 0;

        for (var line: lines) {
            var parts = line.split(" ");
            var color = parts[2].substring(1, 8);
            var distance = Integer.parseInt(color.substring(1, 6), 16);

            var direction = directions.get(index);
            var previousDirection = directions.get((((index - 1) % directions.size()) + directions.size()) % directions.size());
            var nextDirection = directions.get((index + 1) % directions.size());

            if (previousDirection == nextDirection) {
                if (direction == Direction.R || direction == Direction.L) {
                    xDelta = 0;
                } else if (direction == Direction.D || direction == Direction.U) {
                    yDelta = 0;
                }
            } else if (direction == Direction.R) {
                if (previousDirection == Direction.D) {
                    xDelta = -1;
                } else if (previousDirection == Direction.U) {
                    xDelta = 1;
                }
            } else if (direction == Direction.L) {
                if (previousDirection == Direction.D) {
                    xDelta = 1;
                } else if (previousDirection == Direction.U) {
                    xDelta = -1;
                }
            } else if (direction == Direction.D) {
                if (previousDirection == Direction.R) {
                    yDelta = 1;
                } else if (previousDirection == Direction.L) {
                    yDelta = -1;
                }
            } else if (direction == Direction.U) {
                if (previousDirection == Direction.R) {
                    yDelta = -1;
                } else if (previousDirection == Direction.L) {
                    yDelta = 1;
                }
            }

            var nextPosition = switch (directions.get(index)) {
                case R -> new Point2D.Double(position.x + (distance + xDelta), position.y);
                case D -> new Point2D.Double(position.x, position.y + (distance + yDelta));
                case L -> new Point2D.Double(position.x - (distance + xDelta), position.y);
                case U -> new Point2D.Double(position.x, position.y - (distance + yDelta));
            };

            pointsList.add(index, nextPosition);

            position = nextPosition;
            index++;
        }

        var points = pointsList.toArray(new Point2D.Double[0]);

        return new DecimalFormat("#").format(area(points));
    }

    public static double area(Point2D[] polyPoints) {
        int i, j, n = polyPoints.length;
        double area = 0;

        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            area += polyPoints[i].getX() * polyPoints[j].getY();
            area -= polyPoints[j].getX() * polyPoints[i].getY();
        }
        area /= 2.0;
        return (area < 0 ? -area : area);
    }
}
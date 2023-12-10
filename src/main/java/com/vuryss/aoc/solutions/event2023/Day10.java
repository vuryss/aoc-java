package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Point;

import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements DayInterface {
    @Override
    public java.util.Map<String, String> part1Tests() {
        return java.util.Map.of(
            """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
            """,
            "4",
            """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
            """,
            "8"
        );
    }

    @Override
    public java.util.Map<String, String> part2Tests() {
        return java.util.Map.of(
            """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
            """,
            "4",
            """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """,
            "8",
            """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L

            """,
            "10"
        );
    }

    @Override
    public String part1Solution(String input) {
        var pipeMap = constructPipeMap(input);
        var loopDistances = constructLoopDistances(pipeMap);

        return String.valueOf(loopDistances.values().stream().max(Integer::compareTo).orElseThrow());
    }

    @Override
    public String part2Solution(String input) {
        var pipeMap = constructPipeMap(input);
        var loopDistances = constructLoopDistances(pipeMap);
        var count = 0;
        var loopPipes = loopDistances.keySet();

        for (var y = 1; y < pipeMap.lines - 1; y++) {
            for (var x = 1; x < pipeMap.columns - 1; x++) {
                if (loopPipes.contains(new Point(x, y))) {
                    continue;
                }

                // Check if there is odd number of pipes cross to the right, if so - we're inside the loop
                var loopPipeCrossingCount = 0;
                var insidePipe = false;
                CompassDirection pipeCameFrom = null;

                for (var i = x + 1; i < pipeMap.columns; i++) {
                    var p = new Point(i, y);

                    if (!loopPipes.contains(p)) {
                        continue;
                    }

                    var pipe = pipeMap.grid.get(p);

                    if (pipe == Pipe.V) {
                        loopPipeCrossingCount++;
                        continue;
                    }

                    // Here we go "inside" the pipe and store which direction it came from
                    if (pipe == Pipe.NE || pipe == Pipe.SE) {
                        insidePipe = true;
                        pipeCameFrom = pipe == Pipe.NE ? CompassDirection.N : CompassDirection.S;
                        continue;
                    }

                    // If we go outside, and it goes in the same direction, it's not counted as crossing
                    if (insidePipe && pipe != Pipe.H) {
                        loopPipeCrossingCount += (pipe.name().startsWith(pipeCameFrom.name()) ? 0 : 1);
                        insidePipe = false;
                        pipeCameFrom = null;
                    }
                }

                if (loopPipeCrossingCount % 2 != 0) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private PipeMap constructPipeMap(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Pipe>();
        Point start = null;

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                var charAt = lines[y].charAt(x);

                if (charAt == '.') {
                    continue;
                }

                grid.put(new Point(x, y), switch (charAt) {
                    case 'S' -> Pipe.S;
                    case '|' -> Pipe.V;
                    case '-' -> Pipe.H;
                    case 'F' -> Pipe.SE;
                    case '7' -> Pipe.SW;
                    case 'L' -> Pipe.NE;
                    case 'J' -> Pipe.NW;
                    default -> throw new RuntimeException("Unknown pipe: " + charAt);
                });

                if (charAt == 'S') {
                    start = new Point(x, y);
                }
            }
        }

        return new PipeMap(grid, lines.length, lines[0].length(), start);
    }

    private Map<Point, Integer> constructLoopDistances(PipeMap pipeMap) {
        var stepsToLoopPoint = new HashMap<Point, Integer>();
        stepsToLoopPoint.put(pipeMap.start, 0);
        CompassDirection sVerticalConnection = null;
        CompassDirection sHorizontalConnection = null;

        var queue = new LinkedList<Position>();
        queue.add(new Position(pipeMap.start, 0, new HashSet<>(){{ add(pipeMap.start); }}));

        while (!queue.isEmpty()) {
            var position = queue.removeFirst();
            var pipe = pipeMap.grid.get(position.point);

            var visited = new HashSet<>(position.visited);
            visited.add(position.point);

            if (stepsToLoopPoint.containsKey(position.point) && stepsToLoopPoint.get(position.point) < position.steps) {
                continue;
            }

            stepsToLoopPoint.put(position.point, position.steps);

            // Check if there is north connection
            if (pipe == Pipe.V || pipe == Pipe.NE || pipe == Pipe.NW || pipe == Pipe.S) {
                var next = new Point(position.point.x, position.point.y - 1);
                var nextPipe = pipeMap.grid.get(next);

                if ((nextPipe == Pipe.V || nextPipe == Pipe.SE || nextPipe == Pipe.SW) && !visited.contains(next)) {
                    queue.add(new Position(next, position.steps + 1, visited));

                    if (pipe == Pipe.S) {
                        sVerticalConnection = CompassDirection.N;
                    }
                }
            }

            // Check if there is south connection
            if (pipe == Pipe.V || pipe == Pipe.SE || pipe == Pipe.SW || pipe == Pipe.S) {
                var next = new Point(position.point.x, position.point.y + 1);
                var nextPipe = pipeMap.grid.get(next);

                if ((nextPipe == Pipe.V || nextPipe == Pipe.NE || nextPipe == Pipe.NW) && !visited.contains(next)) {
                    queue.add(new Position(next, position.steps + 1, visited));

                    if (pipe == Pipe.S) {
                        sVerticalConnection = CompassDirection.S;
                    }
                }
            }

            // Check if there is east connection
            if (pipe == Pipe.H || pipe == Pipe.NE || pipe == Pipe.SE || pipe == Pipe.S) {
                var next = new Point(position.point.x + 1, position.point.y);
                var nextPipe = pipeMap.grid.get(next);

                if ((nextPipe == Pipe.H || nextPipe == Pipe.NW || nextPipe == Pipe.SW) && !visited.contains(next)) {
                    queue.add(new Position(next, position.steps + 1, visited));

                    if (pipe == Pipe.S) {
                        sHorizontalConnection = CompassDirection.E;
                    }
                }
            }

            // Check if there is west connection
            if (pipe == Pipe.H || pipe == Pipe.NW || pipe == Pipe.SW || pipe == Pipe.S) {
                var next = new Point(position.point.x - 1, position.point.y);
                var nextPipe = pipeMap.grid.get(next);

                if ((nextPipe == Pipe.H || nextPipe == Pipe.NE || nextPipe == Pipe.SE) && !visited.contains(next)) {
                    queue.add(new Position(next, position.steps + 1, visited));

                    if (pipe == Pipe.S) {
                        sHorizontalConnection = CompassDirection.W;
                    }
                }
            }
        }

        // Depending on the Start point connections, we can determine the pipe type
        if (sVerticalConnection != null && sHorizontalConnection != null) {
            pipeMap.grid.put(pipeMap.start, Pipe.valueOf(sVerticalConnection.name() + sHorizontalConnection.name()));
        } else if (sHorizontalConnection == null) {
            pipeMap.grid.put(pipeMap.start, Pipe.V);
        } else {
            pipeMap.grid.put(pipeMap.start, Pipe.H);
        }

        return stepsToLoopPoint;
    }

    private record PipeMap(HashMap<Point, Pipe> grid, int lines, int columns, Point start) {}
    enum Pipe { V, H, SE, SW, NE, NW, S }

    private static class Position {
        public Point point;
        public int steps;
        public Set<Point> visited;

        public Position(Point point, int steps, Set<Point> visited) {
            this.point = point;
            this.steps = steps;
            this.visited = visited;
        }
    }
}

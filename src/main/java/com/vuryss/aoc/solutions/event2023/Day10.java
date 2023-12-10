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
        var loopPipes = constructLoopPipes(pipeMap);

        return String.valueOf(
            loopPipes.values().stream().map(p -> p.distanceFromStart).max(Integer::compareTo).orElseThrow()
        );
    }

    @Override
    public String part2Solution(String input) {
        var pipeMap = constructPipeMap(input);
        var loopPipes = constructLoopPipes(pipeMap);
        var count = 0;

        for (var y = 1; y < pipeMap.lines - 1; y++) {
            for (var x = 1; x < pipeMap.columns - 1; x++) {
                if (loopPipes.containsKey(new Point(x, y))) {
                    continue;
                }

                // Check if there is odd number of pipes cross to the right, if so - we're inside the loop
                var loopPipeCrossingCount = 0;
                var insidePipe = false;
                CompassDirection pipeCameFrom = null;

                for (var i = x + 1; i < pipeMap.columns; i++) {
                    var pipe = pipeMap.grid.get(new Point(i, y));

                    if (pipe == null || !loopPipes.containsKey(pipe.location)) {
                        continue;
                    }

                    if (pipe.type == PipeType.V) {
                        loopPipeCrossingCount++;
                        continue;
                    }

                    // Here we go "inside" the pipe and store which direction it came from
                    if (pipe.type == PipeType.NE || pipe.type == PipeType.SE) {
                        insidePipe = true;
                        pipeCameFrom = pipe.type == PipeType.NE ? CompassDirection.N : CompassDirection.S;
                        continue;
                    }

                    // If we go outside, and it goes in the same direction, it's not counted as crossing
                    if (insidePipe && pipe.type != PipeType.H) {
                        loopPipeCrossingCount += (pipe.type.name().startsWith(pipeCameFrom.name()) ? 0 : 1);
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
                var point = new Point(x, y);

                if (charAt == '.') {
                    continue;
                }

                grid.put(point, switch (charAt) {
                    case 'S' -> new Pipe(null, point, 0);
                    case '|' -> new Pipe(PipeType.V, point, null);
                    case '-' -> new Pipe(PipeType.H, point, null);
                    case 'F' -> new Pipe(PipeType.SE, point, null);
                    case '7' -> new Pipe(PipeType.SW, point, null);
                    case 'L' -> new Pipe(PipeType.NE, point, null);
                    case 'J' -> new Pipe(PipeType.NW, point, null);
                    default -> throw new RuntimeException("Unknown pipe: " + charAt);
                });

                if (charAt == 'S') {
                    start = new Point(x, y);
                }
            }
        }

        return new PipeMap(grid, lines.length, lines[0].length(), start);
    }

    private HashMap<Point, Pipe> constructLoopPipes(PipeMap pipeMap) {
        var startPipe = pipeMap.grid.get(pipeMap.start);
        var queue = new LinkedList<Pipe>(){{ add(startPipe); }};
        var loopPipes = new HashMap<Point, Pipe>(){{ put(startPipe.location, startPipe); }};

        while (!queue.isEmpty()) {
            var pipe = queue.removeFirst();
            loopPipes.put(pipe.location, pipe);

            for (var cp: pipe.connectionPoints().entrySet()) {
                var nextPipe = pipeMap.grid.get(cp.getValue());

                if (nextPipe != null && nextPipe.distanceFromStart == null && nextPipe.connectionPoints().containsKey(cp.getKey().opposite())) {
                    switch (cp.getKey()) {
                        case N -> { pipe.north = nextPipe; nextPipe.south = pipe; }
                        case E -> { pipe.east = nextPipe; nextPipe.west = pipe; }
                        case S -> { pipe.south = nextPipe; nextPipe.north = pipe; }
                        case W -> { pipe.west = nextPipe; nextPipe.east = pipe; }
                    }
                    nextPipe.distanceFromStart = pipe.distanceFromStart + 1;
                    queue.add(nextPipe);
                }
            }
        }

        startPipe.resolveTypeByConnections();

        return loopPipes;
    }

    private record PipeMap(HashMap<Point, Pipe> grid, int lines, int columns, Point start) {}
    enum PipeType { V, H, SE, SW, NE, NW }

    private static class Pipe {
        PipeType type;
        Integer distanceFromStart;
        Point location;
        Pipe north = null;
        Pipe east = null;
        Pipe south = null;
        Pipe west = null;

        public Pipe(PipeType type, Point location, Integer distanceFromStart) {
            this.distanceFromStart = distanceFromStart;
            this.location = location;
            this.type = type;
        }

        public Map<CompassDirection, Point> connectionPoints() {
            return switch (type) {
                case V -> Map.of(CompassDirection.S, location.south(), CompassDirection.N, location.north());
                case H -> Map.of(CompassDirection.E, location.east(), CompassDirection.W, location.west());
                case SE -> Map.of(CompassDirection.S, location.south(), CompassDirection.E, location.east());
                case SW -> Map.of(CompassDirection.S, location.south(), CompassDirection.W, location.west());
                case NE -> Map.of(CompassDirection.N, location.north(), CompassDirection.E, location.east());
                case NW -> Map.of(CompassDirection.N, location.north(), CompassDirection.W, location.west());
                case null -> Map.of(
                    CompassDirection.N, location.north(), CompassDirection.W, location.west(),
                    CompassDirection.S, location.south(), CompassDirection.E, location.east()
                );
            };
        }

        public void resolveTypeByConnections() {
            if (type == null) {
                if (north != null) {
                    type = south != null ? PipeType.V : (east != null ? PipeType.NE : PipeType.NW);
                } else {
                    type = south != null ? (east != null ? PipeType.SE : PipeType.SW) : PipeType.H;
                }
            }
        }
    }

    private static class Position {
        public Point point;
        public int steps;

        public Position(Point point, int steps) {
            this.point = point;
            this.steps = steps;
        }
    }
}

package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """,
            "46"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """,
            "51"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = constructGrid(lines);

        return String.valueOf(calculateEnergized(grid, new Point(0, 0), Direction.R));
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = constructGrid(lines);
        var maxX = lines[0].length() - 1;
        var maxY = lines.length - 1;

        var max = 0;

        for (var x = 0; x <= maxX; x++) {
            grid.values().forEach(Tile::reset);
            max = Math.max(calculateEnergized(grid, new Point(x, 0), Direction.D), max);
            grid.values().forEach(Tile::reset);
            max = Math.max(calculateEnergized(grid, new Point(x, maxY), Direction.U), max);
        }

        for (var y = 0; y <= maxY; y++) {
            grid.values().forEach(Tile::reset);
            max = Math.max(calculateEnergized(grid, new Point(0, y), Direction.R), max);
            grid.values().forEach(Tile::reset);
            max = Math.max(calculateEnergized(grid, new Point(maxX, y), Direction.L), max);
        }

        return String.valueOf(max);
    }

    private HashMap<Point, Tile> constructGrid(String[] lines) {
        var grid = new HashMap<Point, Tile>();

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (var x = 0; x < line.length(); x++) {
                var tile = new Tile();
                tile.position = new Point(x, y);
                tile.type = line.charAt(x);
                grid.put(tile.position, tile);
            }
        }

        return grid;
    }

    private int calculateEnergized(HashMap<Point, Tile> grid, Point startPosition, Direction startDirection) {
        var queue = new LinkedList<Step>();
        queue.add(new Step(startPosition, startDirection));

        while (!queue.isEmpty()) {
            var step = queue.removeFirst();

            if (!grid.containsKey(step.position)) {
                continue;
            }

            var tile = grid.get(step.position);
            tile.isEnergized = true;

            if (tile.beams.contains(step.direction)) {
                continue;
            }

            tile.beams.add(step.direction);

            switch (tile.type) {
                case '|' -> {
                    switch (step.direction) {
                        case R, L -> {
                            queue.add(new Step(step.position.up(), Direction.U));
                            queue.add(new Step(step.position.down(), Direction.D));
                        }
                        case U -> queue.add(new Step(step.position.up(), Direction.U));
                        case D -> queue.add(new Step(step.position.down(), Direction.D));
                    }
                }
                case '-' -> {
                    switch (step.direction) {
                        case R -> queue.add(new Step(step.position.right(), Direction.R));
                        case L -> queue.add(new Step(step.position.left(), Direction.L));
                        case U, D -> {
                            queue.add(new Step(step.position.left(), Direction.L));
                            queue.add(new Step(step.position.right(), Direction.R));
                        }
                    }
                }
                case '/' -> {
                    switch (step.direction) {
                        case R -> queue.add(new Step(step.position.up(), Direction.U));
                        case L -> queue.add(new Step(step.position.down(), Direction.D));
                        case U -> queue.add(new Step(step.position.right(), Direction.R));
                        case D -> queue.add(new Step(step.position.left(), Direction.L));
                    }
                }
                case '\\' -> {
                    switch (step.direction) {
                        case R -> queue.add(new Step(step.position.down(), Direction.D));
                        case L -> queue.add(new Step(step.position.up(), Direction.U));
                        case U -> queue.add(new Step(step.position.left(), Direction.L));
                        case D -> queue.add(new Step(step.position.right(), Direction.R));
                    }
                }
                case '.' -> {
                    switch (step.direction) {
                        case R -> queue.add(new Step(step.position.right(), Direction.R));
                        case L -> queue.add(new Step(step.position.left(), Direction.L));
                        case U -> queue.add(new Step(step.position.up(), Direction.U));
                        case D -> queue.add(new Step(step.position.down(), Direction.D));
                    }
                }
            }
        }

        return grid.values().stream().filter(tile -> tile.isEnergized).toList().size();
    }

    record Step(Point position, Direction direction) {}

    private static class Tile {
        public boolean isEnergized;
        public Character type;
        public Point position;
        public HashSet<Direction> beams = new HashSet<>();

        public Tile() {
            this.isEnergized = false;
            this.type = null;
            this.position = null;
        }

        public void reset() {
            this.isEnergized = false;
            this.beams.clear();
        }
    }
}

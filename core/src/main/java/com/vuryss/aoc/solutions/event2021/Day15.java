package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import java.util.*;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
            """,
            "40"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
            """,
            "315"
        );
    }

    @Override
    public String part1Solution(String input) {
        var grid = new HashMap<Point, Integer>();
        var y = 0;
        var maxY = input.split("\n").length - 1;
        var maxX = input.split("\n")[0].length() - 1;
        var endPoint = new Point(maxX, maxY);

        for (var line: input.split("\n")) {
            for (var x = 0; x < line.length(); x++) {
                grid.put(new Point(x, y), Character.getNumericValue(line.charAt(x)));
            }

            y++;
        }

        var queue = new PriorityQueue<Player>(Comparator.comparingInt(a -> a.risk));
        queue.add(new Player(new Point(0, 0), 0));
        var visited = new HashSet<Point>();

        while (!queue.isEmpty()) {
            var player = queue.poll();

            if (visited.contains(player.point)) {
                continue;
            }

            visited.add(player.point);

            for (var adjacentPoint: player.point.getAdjacentPoints()) {
                if (!grid.containsKey(adjacentPoint) || visited.contains(adjacentPoint)) {
                    continue;
                }

                if (adjacentPoint.equals(endPoint)) {
                    return String.valueOf(player.risk + grid.get(adjacentPoint));
                }

                queue.add(new Player(adjacentPoint, player.risk + grid.get(adjacentPoint)));
            }
        }

        return "";
    }

    @Override
    public String part2Solution(String input) {
        var grid = new HashMap<Point, Integer>();
        var y = 0;
        var maxY = input.split("\n").length;
        var maxX = input.split("\n")[0].length();
        var endPoint = new Point(maxX * 5 - 1, maxY * 5 - 1);

        for (var line: input.split("\n")) {
            for (var x = 0; x < line.length(); x++) {
                grid.put(new Point(x, y), Character.getNumericValue(line.charAt(x)));
            }

            y++;
        }

        var largeGrid = new LargeGrid(grid, maxX, maxY);
        var queue = new PriorityQueue<Player>(Comparator.comparingInt(a -> a.risk));
        queue.add(new Player(new Point(0, 0), 0));
        var visited = new HashSet<Point>();

        while (!queue.isEmpty()) {
            var player = queue.poll();

            if (visited.contains(player.point)) {
                continue;
            }

            visited.add(player.point);

            for (var adjacentPoint: player.point.getAdjacentPoints()) {
                if (!largeGrid.containsKey(adjacentPoint) || visited.contains(adjacentPoint)) {
                    continue;
                }

                if (adjacentPoint.equals(endPoint)) {
                    return String.valueOf(player.risk + largeGrid.get(adjacentPoint));
                }

                queue.add(new Player(adjacentPoint, player.risk + largeGrid.get(adjacentPoint)));
            }
        }

        return "";
    }

    static class LargeGrid {
        HashMap<Point, Integer> grid;
        int sizeX;
        int sizeY;

        public LargeGrid(HashMap<Point, Integer> grid, int sizeX, int sizeY) {
            this.grid = grid;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }

        public boolean containsKey(Point point) {
            return point.x >= 0 && point.x < sizeX * 5 && point.y >= 0 && point.y < sizeY * 5;
        }

        public int get(Point point) {
            var originalPoint = new Point(point.x % sizeX, point.y % sizeY);
            var risk = grid.get(originalPoint) + (point.x / sizeX) + (point.y / sizeY);

            while (risk > 9) {
                risk -= 9;
            }

            return risk;
        }
    }

    static class Player {
        Point point;
        int risk;

        public Player(Point point, int risk) {
            this.point = point;
            this.risk = risk;
        }
    }
}


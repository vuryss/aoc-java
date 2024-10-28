package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
            """,
            "12"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.split("\n");
        var grid = new HashMap<Point, Integer>();

        for (var line: lines) {
            var n = StringUtil.ints(line);

            if (n.get(0).equals(n.get(2))) {
                for (int y = Math.min(n.get(1), n.get(3)), maxY = Math.max(n.get(1), n.get(3)); y <= maxY; y++) {
                    var p = new Point(n.getFirst(), y);

                    grid.put(p, grid.getOrDefault(p, 0) + 1);
                }
            }

            if (n.get(1).equals(n.get(3))) {
                for (int x = Math.min(n.get(0), n.get(2)), maxX = Math.max(n.get(0), n.get(2)); x <= maxX; x++) {
                    var p = new Point(x, n.get(1));

                    grid.put(p, grid.getOrDefault(p, 0) + 1);
                }
            }
        }

        return String.valueOf(grid.values().stream().filter(v -> v > 1).count());
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.split("\n");
        var grid = new HashMap<Point, Integer>();

        for (var line: lines) {
            var n = StringUtil.ints(line);
            var point = new Point(n.get(0), n.get(1));
            var end = new Point(n.get(2), n.get(3));
            var dx = n.get(0) < n.get(2) ? 1 : (n.get(0).equals(n.get(2)) ? 0 : -1);
            var dy = n.get(1) < n.get(3) ? 1 : (n.get(1).equals(n.get(3)) ? 0 : -1);

            while (!point.equals(end)) {
                grid.put(point, grid.getOrDefault(point, 0) + 1);
                point = new Point(point.x + dx, point.y + dy);
            }

            grid.put(end, grid.getOrDefault(end, 0) + 1);
        }

        return String.valueOf(grid.values().stream().filter(v -> v > 1).count());
    }
}

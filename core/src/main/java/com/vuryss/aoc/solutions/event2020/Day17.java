package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point3D;
import com.vuryss.aoc.util.Point4D;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .#.
            ..#
            ###
            """,
            "112"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            .#.
            ..#
            ###
            """,
            "848"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var points = new HashSet<Point3D>();
        var grid = StringUtil.toCharGrid(input);

        for (var y = 0; y < grid.length; y++)
            for (var x = 0; x < grid[y].length; x++)
                if (grid[y][x] == '#') points.add(new Point3D(x, y, 0));

        for (var i = 0; i < 6; i++) {
            var counts = new HashMap<Point3D, Integer>();

            for (var point : points)
                for (var neighbour : point.surrounding())
                    counts.merge(neighbour, 1, Integer::sum);

            var newPoints = new HashSet<Point3D>();

            for (var entry : counts.entrySet()) {
                var point = entry.getKey();
                var count = entry.getValue();

                if (count == 3 || (count == 2 && points.contains(point))) newPoints.add(point);
            }

            points = newPoints;
        }

        return points.size() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var points = new HashSet<Point4D>();
        var grid = StringUtil.toCharGrid(input);

        for (var y = 0; y < grid.length; y++)
            for (var x = 0; x < grid[y].length; x++)
                if (grid[y][x] == '#') points.add(new Point4D(x, y, 0, 0));

        for (var i = 0; i < 6; i++) {
            var counts = new HashMap<Point4D, Integer>();

            for (var point : points)
                for (var neighbour : point.surrounding())
                    counts.merge(neighbour, 1, Integer::sum);

            var newPoints = new HashSet<Point4D>();

            for (var entry : counts.entrySet()) {
                var point = entry.getKey();
                var count = entry.getValue();

                if (count == 3 || (count == 2 && points.contains(point))) newPoints.add(point);
            }

            points = newPoints;
        }

        return points.size() + "";
    }
}

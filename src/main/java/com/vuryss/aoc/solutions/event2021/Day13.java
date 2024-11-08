package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.LetterOCR;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day13 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
            """,
            "17"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var grid = new HashSet<Point>();
        var parts = input.trim().split("\n\n");
        var count = 0;

        for (var line: parts[0].split("\n")) {
            var coords = StringUtil.ints(line);
            grid.add(new Point(coords.get(0), coords.get(1)));
        }

        for (var line: parts[1].split("\n")) {
            var parts2 = line.split("=");
            var axis = parts2[0].charAt(parts2[0].length() - 1);
            var num = Integer.parseInt(parts2[1]);

            grid = fold(grid, axis, num);
            break;
        }

        return grid.size() + "";
    }

    @Override
    public String part2Solution(String input) {
        var grid = new HashSet<Point>();
        var parts = input.trim().split("\n\n");
        var count = 0;

        for (var line: parts[0].split("\n")) {
            var coords = StringUtil.ints(line);
            grid.add(new Point(coords.get(0), coords.get(1)));
        }

        for (var line: parts[1].split("\n")) {
            var parts2 = line.split("=");
            var axis = parts2[0].charAt(parts2[0].length() - 1);
            var num = Integer.parseInt(parts2[1]);

            grid = fold(grid, axis, num);
        }

        return LetterOCR.decode46(grid);
    }

    private HashSet<Point> fold(HashSet<Point> grid, char axis, int num) {
        var newGrid = new HashSet<Point>();

        for (var point: grid) {
            if (axis == 'x' && point.x > num) {
                newGrid.add(new Point(2 * num - point.x, point.y));
            } else if (axis == 'y' && point.y > num) {
                newGrid.add(new Point(point.x, 2 * num - point.y));
            } else {
                newGrid.add(point);
            }
        }

        return newGrid;
    }

    private void printGrid(HashSet<Point> grid) {
        var minx = grid.stream().mapToInt(p -> p.x).min().getAsInt();
        var maxx = grid.stream().mapToInt(p -> p.x).max().getAsInt();
        var miny = grid.stream().mapToInt(p -> p.y).min().getAsInt();
        var maxy = grid.stream().mapToInt(p -> p.y).max().getAsInt();

        for (var y = miny; y <= maxy; y++) {
            for (var x = minx; x <= maxx; x++) {
                if (grid.contains(new Point(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}

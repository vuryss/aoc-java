package com.vuryss.aoc.util;

import java.util.HashMap;

public class Console {
    private static final String ESC = "\u001B[";

    public static void clearLines(long lines) {
        System.out.print(ESC + lines + "F");
        System.out.print(ESC + "J");
    }

    public static void printGrid(HashMap<PointLong, Character> grid, long minX, long minY, long maxX, long maxY) {
        var sb = new StringBuilder();

        for (var row = minY; row <= maxY; row++) {
            for (var col = minX; col <= maxX; col++) {
                var point = new PointLong(col, row);
                sb.append(grid.getOrDefault(point, ' '));
            }
            sb.append('\n');
        }

        System.out.print(sb);
    }
}

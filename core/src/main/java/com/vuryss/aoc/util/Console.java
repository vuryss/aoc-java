package com.vuryss.aoc.util;

import java.util.HashMap;

public class Console {
    private static final String ESC = "\u001B[";

    public static void clearLines(long lines) {
        System.out.print(ESC + lines + "F");
        System.out.print(ESC + "J");
    }

    public static void printGrid(HashMap<PointLong, Integer> grid) {
        long minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (var point : grid.keySet()) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }

        for (var y = minY; y <= maxY; y++) {
            for (var x = minX; x <= maxX; x++) {
                var point = new PointLong(x, y);

                if (!grid.containsKey(point)) {
                    System.out.print(' ');
                    continue;
                }

                switch (grid.get(point)) {
                    case 0 -> System.out.print('#');
                    case 1 -> System.out.print('.');
                    case 2 -> System.out.print('O');
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
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

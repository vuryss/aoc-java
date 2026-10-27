package com.vuryss.aoc.util;

import java.util.HashMap;
import java.util.Set;

public class Util {
    private static final int[][] SURROUNDING_DELTAS_3D = createSurroundingDeltas3d();
    private static final int[][] SURROUNDING_DELTAS_4D = createSurroundingDeltas4d();

    private static int[][] createSurroundingDeltas3d() {
        var deltas = new int[26][3];
        int i = 0;

        for (var x = -1; x <= 1; x++)
            for (var y = -1; y <= 1; y++)
                for (var z = -1; z <= 1; z++)
                    if (x != 0 || y != 0 || z != 0)
                        deltas[i++] = new int[]{x, y, z};

        return deltas;
    }

    public static int[][] getSurroundingDeltas3d() {
        return SURROUNDING_DELTAS_3D;
    }

    private static int[][] createSurroundingDeltas4d() {
        var deltas = new int[80][4];
        int i = 0;

        for (var x = -1; x <= 1; x++)
            for (var y = -1; y <= 1; y++)
                for (var z = -1; z <= 1; z++)
                    for (var w = -1; w <= 1; w++)
                        if (x != 0 || y != 0 || z != 0 || w != 0)
                            deltas[i++] = new int[]{x, y, z, w};

        return deltas;
    }

    public static int[][] getSurroundingDeltas4d() {
        return SURROUNDING_DELTAS_4D;
    }

    public static HashMap<Point, Character> inputToGrid(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Character>();

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                grid.put(new Point(x, y), lines[y].charAt(x));
            }
        }

        return grid;
    }

    public static HashMap<Point, Character> inputToGrid(String input, Set<Character> withoutCharacters) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Character>();

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (!withoutCharacters.contains(lines[y].charAt(x))) {
                    grid.put(new Point(x, y), lines[y].charAt(x));
                }
            }
        }

        return grid;
    }
}

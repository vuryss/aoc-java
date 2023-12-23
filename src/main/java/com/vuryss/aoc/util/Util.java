package com.vuryss.aoc.util;

import java.util.HashMap;
import java.util.Set;

public class Util {
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

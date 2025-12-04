package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Util;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """,
            "13"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """,
            "43"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = Util.inputToGrid(input);
        var count = 0;

        for (var entry: grid.entrySet()) {
            if (entry.getValue() == '@') {
                var countAdjacent = 0;

                for (var point: entry.getKey().surroundingPoints()) {
                    if (grid.getOrDefault(point, '.') == '@') {
                        countAdjacent++;
                    }
                }

                if (countAdjacent < 4) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = Util.inputToGrid(input);
        var count = 0;
        var newGrid = new HashMap<Point, Character>();
        var removed = true;

        while (removed) {
            removed = false;
            newGrid = new HashMap<>(grid);

            for (var entry: grid.entrySet()) {
                if (entry.getValue() == '@') {
                    var countAdjacent = 0;

                    for (var point: entry.getKey().surroundingPoints()) {
                        if (grid.getOrDefault(point, '.') == '@') {
                            countAdjacent++;
                        }
                    }

                    if (countAdjacent < 4) {
                        count++;
                        newGrid.put(entry.getKey(), '.');
                        removed = true;
                    }
                }
            }

            grid = newGrid;
        }

        return String.valueOf(count);
    }
}

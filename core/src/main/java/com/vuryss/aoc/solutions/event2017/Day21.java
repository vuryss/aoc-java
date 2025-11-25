package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MatrixUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
    private final int[][] twoVariants = new int[3][4];
    private final int[][] threeVariants = new int[7][9];

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ../.# => ##./#../...
            .#./..#/### => #..#/..../..../#..#
            """,
            "12"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var iterations = isTest ? 2 : 5;

        return solve(input, iterations);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, 18);
    }

    private String solve(String input, int iterations) {
        var rules = parseRules(input);
        var grid = new char[][] {{'.', '#', '.'}, {'.', '.', '#'}, {'#', '#', '#'}};

        for (var i = 0; i < iterations; i++) {
            int chuckSize = grid.length % 2 == 0 ? 2 : 3;
            int subSections = grid.length / chuckSize;
            var newGrid = new char[subSections * (chuckSize + 1)][subSections * (chuckSize + 1)];

            for (var y = 0; y < subSections; y++) {
                for (var x = 0; x < subSections; x++) {
                    // Construct the input rule from the subsection lines
                    var rule = new char[chuckSize * chuckSize];

                    for (var yy = 0; yy < chuckSize; yy++) {
                        System.arraycopy(grid[y * chuckSize + yy], x * chuckSize, rule, yy * chuckSize, chuckSize);
                    }

                    // Apply the rule
                    var output = rules.get(new String(rule));

                    // Copy the output to the new grid
                    for (var yy = 0; yy < chuckSize + 1; yy++) {
                        for (var xx = 0; xx < chuckSize + 1; xx++) {
                            newGrid[y * (chuckSize + 1) + yy][x * (chuckSize + 1) + xx] = output.charAt(yy * (chuckSize + 1) + xx);
                        }
                    }
                }
            }

            grid = newGrid;
        }

        var count = 0;

        for (var row: grid) {
            for (var c: row) {
                if (c == '#') {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private Map<String, String> parseRules(String input) {
        generateVariants();
        input = input.trim().replace("/", "");
        var rules = new HashMap<String, String>();

        for (var line: input.split("\n")) {
            var parts = line.split(" => ");
            rules.put(parts[0], parts[1]);
        }

        var extendedRules = new HashMap<>(rules);

        for (var es: rules.entrySet()) {
            if (es.getKey().length() == 4) {
                var currentChars = es.getKey().toCharArray();

                for (var variant: twoVariants) {
                    var chars = new char[4];
                    for (var i = 0; i < 4; i++) {
                        chars[i] = currentChars[variant[i]];
                    }
                    extendedRules.computeIfAbsent(new String(chars), k -> es.getValue());
                }

                continue;
            }

            for (var variant: threeVariants) {
                var chars = new char[9];
                for (var i = 0; i < 9; i++) {
                    chars[i] = es.getKey().charAt(variant[i]);
                }
                extendedRules.computeIfAbsent(new String(chars), k -> es.getValue());
            }
        }

        return extendedRules;
    }

    private void generateVariants() {
        var twoByTwo = new int[][]{{0, 1}, {2, 3}};

        for (var i = 0; i < 3; i++) {
            twoByTwo = MatrixUtil.rotateClockwise(twoByTwo);
            twoVariants[i] = MatrixUtil.flatten(twoByTwo);
        }

        var threeByThree = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        var threeByThreeFlipped = new int[][]{{2, 1, 0}, {5, 4, 3}, {8, 7, 6}};
        threeVariants[6] = MatrixUtil.flatten(threeByThreeFlipped);

        for (var i = 0; i < 3; i++) {
            threeByThree = MatrixUtil.rotateClockwise(threeByThree);
            threeByThreeFlipped = MatrixUtil.rotateClockwise(threeByThreeFlipped);
            threeVariants[i] = MatrixUtil.flatten(threeByThree);
            threeVariants[i + 3] = MatrixUtil.flatten(threeByThreeFlipped);
        }
    }
}

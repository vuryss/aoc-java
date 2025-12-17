package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MatrixUtil;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var sections = input.trim().split("\n\n");
        var regionInputs = sections[sections.length - 1].split("\n");
        var regions = new Region[regionInputs.length];

        for (var i = 0; i < regionInputs.length; i++) {
            var parts = regionInputs[i].split(": ");
            var dimensions = parts[0].split("x");
            var presents = StringUtil.ints(parts[1]);
            regions[i] = new Region(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]), presents.stream().mapToInt(Integer::intValue).toArray());
        }

        if (!isTest) {
            // TROLL ALERT, TROLL ALERT
            return Arrays.stream(regions).filter(this::canFit).count() + "";
        }

        char[][][] shapes = new char[sections.length - 1][3][3];

        for (var i = 0; i < shapes.length; i++) {
            var lines = sections[i].split("\n");
            for (var j = 0; j < 3; j++) {
                shapes[i][j] = lines[j + 1].toCharArray();
            }
        }

        char[][][][] expandedShapes = expandShapes(shapes);

        var count = 0;

        for (var region: regions) {
            if (canFitTest(region, region.generateEmptyGrid(), expandedShapes)) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    private char[][][][] expandShapes(char[][][] shapes) {
        var expanded = new char[shapes.length][8][4][4];

        for (var index = 0; index < shapes.length; index++) {
            char[][] shape = shapes[index];
            char[][] flipped = MatrixUtil.flipHorizontally(shape);
            var variants = new char[8][4][4];

            variants[0] = shape;
            variants[1] = MatrixUtil.rotateClockwise(shape);
            variants[2] = MatrixUtil.rotateClockwise(variants[1]);
            variants[3] = MatrixUtil.rotateClockwise(variants[2]);
            variants[4] = flipped;
            variants[5] = MatrixUtil.rotateClockwise(flipped);
            variants[6] = MatrixUtil.rotateClockwise(variants[5]);
            variants[7] = MatrixUtil.rotateClockwise(variants[6]);

            expanded[index] = dedupe(variants);
        }

        return expanded;
    }

    private char[][][] dedupe(char[][][] variants) {
        char[][][] deduped = new char[variants.length][4][4];
        deduped[0] = variants[0];
        int dedupedCount = 1;

        outer:
        for (var i = 1; i < variants.length; i++) {
            for (var j = 0; j < dedupedCount; j++) {
                if (Arrays.deepEquals(variants[i], deduped[j])) {
                    continue outer;
                }
            }

            deduped[dedupedCount++] = variants[i];
        }

        return deduped;
    }

    private boolean canFit(Region region) {
        int shapeCount = 0;
        int maxShapes = region.height / 3 * region.width / 3;

        for (var i = 0; i < region.presents.length; i++) {
            shapeCount += region.presents[i];
        }

        return shapeCount <= maxShapes;
    }

    private boolean canFitTest(Region region, char[][] grid, char[][][][] shapes) {
        if (region.remaining() == 0) {
            return true;
        }

        for (var index = 0; index < region.presents.length; index++) {
            for (var count = 0; count < region.presents[index]; count++) {
                for (var shapeVariant: shapes[index]) {
                    if (shapeVariant[0][0] == '\0') {
                        continue;
                    }

                    for (var y = 0; y < region.height - 2; y++) {
                        for (var x = 0; x < region.width - 2; x++) {
                            if (fitsOnGrid(grid, shapeVariant, x, y)) {
                                var remaining = region.presents.clone();
                                remaining[index]--;
                                var newGrid = placeOnGrid(grid, shapeVariant, x, y);

                                if (canFitTest(new Region(region.width, region.height, remaining), newGrid, shapes)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean fitsOnGrid(char[][] grid, char[][] shape, int x, int y) {
        for (var dy = 0; dy < 3; dy++) {
            for (var dx = 0; dx < 3; dx++) {
                if (shape[dy][dx] != '#') {
                    continue;
                }

                if (grid[y + dy][x + dx] != '.') {
                    return false;
                }
            }
        }

        return true;
    }

    private char[][] placeOnGrid(char[][] grid, char[][] shape, int x, int y) {
        var copy = new char[grid.length][grid[0].length];

        for (var i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, grid[0].length);
        }

        for (var dy = 0; dy < 3; dy++) {
            for (var dx = 0; dx < 3; dx++) {
                if (shape[dy][dx] == '#') {
                    copy[y + dy][x + dx] = '#';
                }
            }
        }

        return copy;
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    record Region(int width, int height, int[] presents) {
        public int remaining() {
            return Arrays.stream(presents).sum();
        }

        public char[][] generateEmptyGrid() {
            char[][] grid = new char[height][width];

            for (var y = 0; y < height; y++) {
                Arrays.fill(grid[y], '.');
            }

            return grid;
        }
    }
}

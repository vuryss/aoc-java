package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            30373
            25512
            65332
            33549
            35390
            """,
            "21"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            30373
            25512
            65332
            33549
            35390
            """,
            "8"
        );
    }

    @Override
    public String part1Solution(String input) {
        int[][] grid = createTreeGrid(input);
        int visibleCount = 0;

        for (var lineIndex = 0; lineIndex < grid.length; lineIndex++) {
            for (var columnIndex = 0; columnIndex < grid[lineIndex].length; columnIndex++) {
                if (isVisible(grid, lineIndex, columnIndex)) {
                    visibleCount++;
                }
            }
        }

        return String.valueOf(visibleCount);
    }

    @Override
    public String part2Solution(String input) {
        int[][] grid = createTreeGrid(input);
        int maxScenicScore = 0;

        for (var lineIndex = 0; lineIndex < grid.length; lineIndex++) {
            for (var columnIndex = 0; columnIndex < grid[lineIndex].length; columnIndex++) {
                maxScenicScore = Math.max(maxScenicScore, scenicScore(grid, lineIndex, columnIndex));
            }
        }

        return String.valueOf(maxScenicScore);
    }

    private int[][] createTreeGrid(String input) {
        var lines = input.trim().split("\n");
        int[][] grid = new int[lines.length][lines[0].length()];

        for (var lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            var lineCharacters = lines[lineIndex].trim().toCharArray();
            for (var columnIndex = 0; columnIndex < lines[0].length(); columnIndex++) {
                grid[lineIndex][columnIndex] = Character.getNumericValue(lineCharacters[columnIndex]);
            }
        }

        return grid;
    }

    private int scenicScore(int[][] grid, int row, int column) {
        int top = 0, right = 0, bottom = 0, left = 0;
        int height = grid[row][column];

        // Calculate top score
        for (int gridRow = row - 1; gridRow >= 0; gridRow--) {
            top++;

            if (grid[gridRow][column] >= height) {
                break;
            }
        }

        // Calculate bottom score
        for (int gridRow = row + 1; gridRow < grid.length; gridRow++) {
            bottom++;

            if (grid[gridRow][column] >= height) {
                break;
            }
        }

        // Calculate left score
        for (int gridColumn = column - 1; gridColumn >= 0; gridColumn--) {
            left++;

            if (grid[row][gridColumn] >= height) {
                break;
            }
        }

        // Calculate right score
        for (int gridColumn = column + 1; gridColumn < grid[row].length; gridColumn++) {
            right++;

            if (grid[row][gridColumn] >= height) {
                break;
            }
        }

        return top * right * bottom * left;
    }

    private boolean isVisible(int[][] grid, int row, int column) {
        // Edges of the grid are always visible
        if (row == 0 || row == grid.length - 1 || column == 0 || column == grid[0].length - 1) {
            return true;
        }

        int height = grid[row][column];
        boolean visible;

        // Check top visibility
        visible = true;

        for (int gridRow = 0; gridRow < row; gridRow++) {
            if (grid[gridRow][column] >= height) {
                visible = false;
                break;
            }
        }

        if (visible) {
            return true;
        }

        // Check bottom visibility
        visible = true;

        for (int gridRow = row + 1; gridRow < grid.length; gridRow++) {
            if (grid[gridRow][column] >= height) {
                visible = false;
                break;
            }
        }

        if (visible) {
            return true;
        }

        // Check left visibility
        visible = true;

        for (int gridColumn = 0; gridColumn < column; gridColumn++) {
            if (grid[row][gridColumn] >= height) {
                visible = false;
                break;
            }
        }

        if (visible) {
            return true;
        }

        // Check right visibility
        visible = true;

        for (int gridColumn = column + 1; gridColumn < grid[row].length; gridColumn++) {
            if (grid[row][gridColumn] >= height) {
                visible = false;
                break;
            }
        }

        return visible;
    }
}

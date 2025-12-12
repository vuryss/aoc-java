package com.vuryss.aoc.util;

public class MatrixUtil {
    // Not straightforward to make generic, so if we need more types - copy/pasta FTW
    public static int[][] rotateClockwise(int[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        var rotated = new int[m][n];

        for (var i = 0; i < n; i++) {
            for (var j = 0; j < m; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    public static char[][] rotateClockwise(char[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        var rotated = new char[m][n];

        for (var i = 0; i < n; i++) {
            for (var j = 0; j < m; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    public static char[][] flipHorizontally(char[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        var flipped = new char[n][m];

        for (var i = 0; i < n; i++) {
            for (var j = 0; j < m; j++) {
                flipped[i][j] = matrix[i][m - 1 - j];
            }
        }

        return flipped;
    }

    public static int[] flatten(int[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        var flattened = new int[n * m];

        for (var i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, flattened, i * m, m);
        }

        return flattened;
    }

    public static char[] flatten(char[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        var flattened = new char[n * m];

        for (var i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, flattened, i * m, m);
        }

        return flattened;
    }
}

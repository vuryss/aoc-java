package com.vuryss.aoc.util;

public class ArrayUtil {
    public static int max(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        int max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    public static long max(long[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        long max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    public static int maxItemIndex(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        int max = array[0];
        int maxIndex = 0;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    public static char[] reverse(char[] array) {
        var reversed = array.clone();

        for (int i = 0, j = reversed.length - 1; i < j; i++, j--) {
            char tmp = reversed[i];
            reversed[i] = reversed[j];
            reversed[j] = tmp;
        }

        return reversed;
    }
}

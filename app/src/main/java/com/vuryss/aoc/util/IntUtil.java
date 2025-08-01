package com.vuryss.aoc.util;

public class IntUtil {
    public static Integer parseUnsignedInteger(String s) {
        return Integer.parseInt(s.replaceAll("\\D+", ""));
    }

    public static Integer parseSignedInteger(String s) {
        return Integer.parseInt(s.replaceAll("[^\\d-]+", ""));
    }
}

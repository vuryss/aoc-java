package com.vuryss.aoc.util;

public enum FlatTopHexDirection {
    N, NE, SE, S, SW, NW;

    public static FlatTopHexDirection from(String direction) {
        return switch (direction) {
            case "n" -> N;
            case "ne" -> NE;
            case "se" -> SE;
            case "s" -> S;
            case "sw" -> SW;
            case "nw" -> NW;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }
}


package com.vuryss.aoc.util;

public enum PointyTopHexDirection {
    E, NE, SE, W, SW, NW;

    public static PointyTopHexDirection from(String direction) {
        return switch (direction) {
            case "e" -> E;
            case "ne" -> NE;
            case "se" -> SE;
            case "w" -> W;
            case "sw" -> SW;
            case "nw" -> NW;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }
}

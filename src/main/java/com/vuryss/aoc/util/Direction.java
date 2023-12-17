package com.vuryss.aoc.util;

public enum Direction {
    R, L, U, D;

    public Direction opposite() {
        return switch (this) {
            case R -> L;
            case L -> R;
            case U -> D;
            case D -> U;
        };
    }

    public Direction turnLeft() {
        return switch (this) {
            case R -> U;
            case L -> D;
            case U -> L;
            case D -> R;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case R -> D;
            case L -> U;
            case U -> R;
            case D -> L;
        };
    }

    public String toString() {
        return switch (this) {
            case R -> ">";
            case L -> "<";
            case U -> "^";
            case D -> "v";
        };
    }
}

package com.vuryss.aoc.util;

import org.jetbrains.annotations.NotNull;

public enum Direction {
    R, L, U, D;

    public static Direction fromChar(char c) {
        return switch (c) {
            case 'R' -> R;
            case 'L' -> L;
            case 'U' -> U;
            case 'D' -> D;
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    public static Direction fromArrowChar(char c) {
        return switch (c) {
            case '>' -> R;
            case '<' -> L;
            case '^' -> U;
            case 'v' -> D;
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    public Direction opposite() {
        return switch (this) {
            case R -> L;
            case L -> R;
            case U -> D;
            case D -> U;
        };
    }

    @NotNull
    public Direction turnLeft() {
        return switch (this) {
            case R -> U;
            case L -> D;
            case U -> L;
            case D -> R;
        };
    }

    @NotNull
    public Direction turnRight() {
        return switch (this) {
            case R -> D;
            case L -> U;
            case U -> R;
            case D -> L;
        };
    }

    @NotNull
    public String getChar() {
        return switch (this) {
            case R -> "R";
            case L -> "L";
            case U -> "U";
            case D -> "D";
        };
    }

    @NotNull
    public String toString() {
        return switch (this) {
            case R -> ">";
            case L -> "<";
            case U -> "^";
            case D -> "v";
        };
    }
}

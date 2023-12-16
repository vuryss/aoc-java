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
}

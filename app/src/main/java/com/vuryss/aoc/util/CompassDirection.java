package com.vuryss.aoc.util;

public enum CompassDirection {
    N, E, S, W, NE, SE, SW, NW;

    public CompassDirection opposite() {
        return switch (this) {
            case N -> S;
            case E -> W;
            case S -> N;
            case W -> E;
            case NE -> SW;
            case SE -> NW;
            case SW -> NE;
            case NW -> SE;
        };
    }
}

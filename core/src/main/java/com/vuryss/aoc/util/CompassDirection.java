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

    public CompassDirection turnLeft90() {
        return switch (this) {
            case N -> W;
            case E -> N;
            case S -> E;
            case W -> S;
            case NE -> NW;
            case SE -> NE;
            case SW -> SE;
            case NW -> SW;
        };
    }

    public CompassDirection turnRight90() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
            case NE -> SE;
            case SE -> SW;
            case SW -> NW;
            case NW -> NE;
        };
    }
}

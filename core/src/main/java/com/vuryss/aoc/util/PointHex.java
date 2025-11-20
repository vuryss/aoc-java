package com.vuryss.aoc.util;

import java.util.List;

public class PointHex {
    public long x;
    public long y;
    public long z;

    public PointHex(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public List<PointHex> adjacent() {
        return List.of(
            new PointHex(x, y + 1, z - 1),
            new PointHex(x + 1, y, z - 1),
            new PointHex(x + 1, y - 1, z),
            new PointHex(x, y - 1, z + 1),
            new PointHex(x - 1, y, z + 1),
            new PointHex(x - 1, y + 1, z)
        );
    }

    public long distanceTo(PointHex other) {
        return (Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)) / 2;
    }

    public PointHex goInDirection(HexDirection direction) {
        return goInDirection(direction, 1);
    }

    public PointHex goInDirection(HexDirection direction, int distance) {
        return switch (direction) {
            case N -> new PointHex(x, y + distance, z - distance);
            case NE -> new PointHex(x + distance, y, z - distance);
            case SE -> new PointHex(x + distance, y - distance, z);
            case S -> new PointHex(x, y - distance, z + distance);
            case SW -> new PointHex(x - distance, y, z + distance);
            case NW -> new PointHex(x - distance, y + distance, z);
        };
    }
}

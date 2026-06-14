package com.vuryss.aoc.util;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Point4D {
    public long x;
    public long y;
    public long z;
    public long w;

    public Point4D(long x, long y, long z, long w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public long manhattanDistance(Point4D point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z)  + Math.abs(w - point.w);
    }
}

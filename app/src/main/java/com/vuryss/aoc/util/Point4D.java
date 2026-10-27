package com.vuryss.aoc.util;

public record Point4D(long x, long y, long z, long w) {
    public Point4D[] surrounding() {
        var deltas = Util.getSurroundingDeltas4d();
        var surrounding = new Point4D[deltas.length];

        for (var i = 0; i < deltas.length; i++) {
            surrounding[i] = new Point4D(x + deltas[i][0], y + deltas[i][1], z + deltas[i][2], w + deltas[i][3]);
        }

        return surrounding;
    }

    public Point4D min(Point4D other) {
        return new Point4D(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z), Math.min(w, other.w));
    }

    public Point4D max(Point4D other) {
        return new Point4D(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z), Math.max(w, other.w));
    }

    public long manhattanDistance(Point4D point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z)  + Math.abs(w - point.w);
    }
}

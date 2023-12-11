package com.vuryss.aoc.util;

import com.google.common.base.Objects;

import java.util.List;

public class PointLong {
    public long x;
    public long y;

    public PointLong(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public PointLong add(PointLong delta) {
        return new PointLong(x + delta.x, y + delta.y);
    }

    public List<PointLong> surroundingPoints() {
        return List.of(
            new PointLong(x - 1, y - 1),
            new PointLong(x, y - 1),
            new PointLong(x + 1, y - 1),
            new PointLong(x - 1, y),
            new PointLong(x + 1, y),
            new PointLong(x - 1, y + 1),
            new PointLong(x, y + 1),
            new PointLong(x + 1, y + 1)
        );
    }

    public List<PointLong> getAdjacentPoints() {
        return List.of(
            new PointLong(x - 1, y),
            new PointLong(x + 1, y),
            new PointLong(x, y - 1),
            new PointLong(x, y + 1)
        );
    }

    public PointLong north() {
        return new PointLong(x, y - 1);
    }

    public PointLong east() {
        return new PointLong(x + 1, y);
    }

    public PointLong south() {
        return new PointLong(x, y + 1);
    }

    public PointLong west() {
        return new PointLong(x - 1, y);
    }

    public long manhattan(PointLong p) {
        return PointLong.manhattan(this, p);
    }

    public static long manhattan(PointLong a, PointLong b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointLong point = (PointLong) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}

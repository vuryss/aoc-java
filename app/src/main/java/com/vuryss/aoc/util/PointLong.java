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

    public PointLong sub(PointLong delta) {
        return new PointLong(x - delta.x, y - delta.y);
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

    // Top, Left, Right, Bottom to keep order line then column
    public PointLong[] adjacentTopLeftOrder() {
        return new PointLong[] {
            new PointLong(x, y - 1),
            new PointLong(x - 1, y),
            new PointLong(x + 1, y),
            new PointLong(x, y + 1)
        };
    }

    public List<PointLong> getAdjacentPoints() {
        return List.of(
            new PointLong(x - 1, y), // Left
            new PointLong(x + 1, y), // Right
            new PointLong(x, y - 1), // Top
            new PointLong(x, y + 1)  // Bottom
        );
    }

    // Directional movement methods (grouped for readability)
    public PointLong north() { return north(1); }
    public PointLong north(int distance) { return new PointLong(x, y - distance); }

    public PointLong east() { return east(1); }
    public PointLong east(int distance) { return new PointLong(x + distance, y); }

    public PointLong south() { return south(1); }
    public PointLong south(int distance) { return new PointLong(x, y + distance); }

    public PointLong west() { return west(1); }
    public PointLong west(int distance) { return new PointLong(x - distance, y); }

    public PointLong right() { return right(1); }
    public PointLong right(int distance) { return new PointLong(x + distance, y); }

    public PointLong left() { return left(1); }
    public PointLong left(int distance) { return new PointLong(x - distance, y); }

    public PointLong up() { return up(1); }
    public PointLong up(int distance) { return new PointLong(x, y - distance); }

    public PointLong down() { return down(1); }
    public PointLong down(int distance) { return new PointLong(x, y + distance); }

    public PointLong goInDirection(Direction direction) {
        return goInDirection(direction, 1);
    }

    public PointLong goInDirection(Direction direction, int distance) {
        return switch (direction) {
            case U -> up(distance);
            case D -> down(distance);
            case L -> left(distance);
            case R -> right(distance);
        };
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

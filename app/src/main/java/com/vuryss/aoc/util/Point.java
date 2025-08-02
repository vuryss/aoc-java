package com.vuryss.aoc.util;

import java.util.List;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point delta) {
        return new Point(x + delta.x, y + delta.y);
    }

    public List<Point> surroundingPoints() {
        return List.of(
            new Point(x - 1, y - 1),
            new Point(x, y - 1),
            new Point(x + 1, y - 1),
            new Point(x - 1, y),
            new Point(x + 1, y),
            new Point(x - 1, y + 1),
            new Point(x, y + 1),
            new Point(x + 1, y + 1)
        );
    }

    public List<Point> getAdjacentPoints() {
        return List.of(
            new Point(x - 1, y),
            new Point(x + 1, y),
            new Point(x, y - 1),
            new Point(x, y + 1)
        );
    }

    // Directional movement methods (grouped for readability)
    public Point north() { return north(1); }
    public Point north(int distance) { return new Point(x, y - distance); }

    public Point east() { return east(1); }
    public Point east(int distance) { return new Point(x + distance, y); }

    public Point south() { return south(1); }
    public Point south(int distance) { return new Point(x, y + distance); }

    public Point west() { return west(1); }
    public Point west(int distance) { return new Point(x - distance, y); }

    public Point right() { return right(1); }
    public Point right(int distance) { return new Point(x + distance, y); }

    public Point left() { return left(1); }
    public Point left(int distance) { return new Point(x - distance, y); }

    public Point up() { return up(1); }
    public Point up(int distance) { return new Point(x, y - distance); }

    public Point down() { return down(1); }
    public Point down(int distance) { return new Point(x, y + distance); }

    // Directional-from-enum methods (grouped for readability)
    public Point forwardFromCompassDirection(CompassDirection direction) {
        return forwardFromCompassDirection(direction, 1);
    }
    public Point forwardFromCompassDirection(CompassDirection direction, int distance) {
        return switch (direction) {
            case N -> north(distance);
            case E -> east(distance);
            case S -> south(distance);
            case W -> west(distance);
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    public Point leftFromDirection(Direction direction) {
        return leftFromDirection(direction, 1);
    }
    public Point leftFromDirection(Direction direction, int distance) {
        return switch (direction) {
            case U -> left(distance);
            case D -> right(distance);
            case L -> down(distance);
            case R -> up(distance);
        };
    }

    public Point rightFromDirection(Direction direction) {
        return rightFromDirection(direction, 1);
    }
    public Point rightFromDirection(Direction direction, int distance) {
        return switch (direction) {
            case U -> right(distance);
            case D -> left(distance);
            case L -> up(distance);
            case R -> down(distance);
        };
    }

    public Point forwardFromDirection(Direction direction) {
        return forwardFromDirection(direction, 1);
    }
    public Point forwardFromDirection(Direction direction, int distance) {
        return switch (direction) {
            case U -> up(distance);
            case D -> down(distance);
            case L -> left(distance);
            case R -> right(distance);
        };
    }

    public static int manhattan(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public int manhattan(Point p) {
        return Point.manhattan(this, p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}

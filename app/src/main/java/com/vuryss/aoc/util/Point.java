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

    public Point north() {
        return new Point(x, y - 1);
    }

    public Point east() {
        return new Point(x + 1, y);
    }

    public Point south() {
        return new Point(x, y + 1);
    }

    public Point west() {
        return new Point(x - 1, y);
    }

    public Point right() {
        return new Point(x + 1, y);
    }

    public Point left() {
        return new Point(x - 1, y);
    }

    public Point up() {
        return new Point(x, y - 1);
    }

    public Point down() {
        return new Point(x, y + 1);
    }

    public Point leftFromDirection(Direction direction) {
        return switch (direction) {
            case U -> left();
            case D -> right();
            case L -> down();
            case R -> up();
        };
    }

    public Point rightFromDirection(Direction direction) {
        return switch (direction) {
            case U -> right();
            case D -> left();
            case L -> up();
            case R -> down();
        };
    }

    public Point forwardFromDirection(Direction direction) {
        return switch (direction) {
            case U -> up();
            case D -> down();
            case L -> left();
            case R -> right();
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

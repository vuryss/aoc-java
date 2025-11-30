package com.vuryss.aoc.util;

import java.util.List;

public class GridUtil {
    public static Point[] rectanglePerimeter(int x, int y, int width, int height) {
        var points = new Point[width * 2 + height * 2 - 4];
        var index = 0;
        var right = x + width - 1;
        var bottom = y + height - 1;

        // Top line
        for (var i = x; i < right; i++) {
            points[index++] = new Point(i, y);
        }

        // Right line
        for (var i = y; i < bottom; i++) {
            points[index++] = new Point(right, i);
        }

        // Bottom line (right to left, reverse order)
        for (var i = right; i > x; i--) {
            points[index++] = new Point(i, bottom);
        }

        // Left line (bottom to top, reverse order)
        for (var i = bottom; i > y; i--) {
            points[index++] = new Point(x, i);
        }

        return points;
    }

    public static Point[] rectanglePerimeter(BoundingBox boundingBox) {
        return rectanglePerimeter(boundingBox.minX, boundingBox.minY, boundingBox.width(), boundingBox.height());
    }

    public static BoundingBox getBoundingBox(List<Point> points) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point point: points) {
            if (point.x < minX) minX = point.x;
            if (point.y < minY) minY = point.y;
            if (point.x > maxX) maxX = point.x;
            if (point.y > maxY) maxY = point.y;
        }

        return new BoundingBox(minX, minY, maxX, maxY);
    }

    public static BoundingBox getBoundingBox(Point[] points) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point point: points) {
            if (point.x < minX) minX = point.x;
            if (point.y < minY) minY = point.y;
            if (point.x > maxX) maxX = point.x;
            if (point.y > maxY) maxY = point.y;
        }

        return new BoundingBox(minX, minY, maxX, maxY);
    }
}


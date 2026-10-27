package com.vuryss.aoc.util;

public class BoundingBox {
    public int minX;
    public int minY;
    public int maxX;
    public int maxY;

    public BoundingBox(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public int width() {
        return maxX - minX + 1;
    }

    public int height() {
        return maxY - minY + 1;
    }
}

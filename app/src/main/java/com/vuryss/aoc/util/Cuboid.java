package com.vuryss.aoc.util;

public class Cuboid {
    public final Point3D minPoint;
    public final Point3D maxPoint;
    private long numberOfPoints = -1L;

    public Cuboid(Point3D minPoint, Point3D maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public long numberOfPoints() {
        if (numberOfPoints == -1) {
            numberOfPoints = xPoints() * yPoints() * zPoints();
        }

        return numberOfPoints;
    }

    public long xPoints() {
        return maxPoint.x - minPoint.x + 1;
    }

    public long yPoints() {
        return maxPoint.y - minPoint.y + 1;
    }

    public long zPoints() {
        return maxPoint.z - minPoint.z + 1;
    }

    public long shortestManhattanDistance(Point3D point) {
        return Math.max(0, Math.max(minPoint.x - point.x, point.x - maxPoint.x))
             + Math.max(0, Math.max(minPoint.y - point.y, point.y - maxPoint.y))
             + Math.max(0, Math.max(minPoint.z - point.z, point.z - maxPoint.z));
    }
}

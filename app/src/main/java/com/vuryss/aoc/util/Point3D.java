package com.vuryss.aoc.util;

public class Point3D implements Cloneable {
    public Long x;
    public Long y;
    public Long z;

    public Point3D(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D add(Point3D delta) {
        return new Point3D(x + delta.x, y + delta.y, z + delta.z);
    }

    public Point3D sub(Point3D delta) {
        return new Point3D(x - delta.x, y - delta.y, z - delta.z);
    }

    public Point3D min(Point3D other) {
        return new Point3D(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z));
    }

    public Point3D max(Point3D other) {
        return new Point3D(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z));
    }

    public Point3D[] surrounding() {
        var deltas = Util.getSurroundingDeltas3d();
        var surrounding = new Point3D[deltas.length];

        for (var i = 0; i < deltas.length; i++) {
            surrounding[i] = new Point3D(x + deltas[i][0], y + deltas[i][1], z + deltas[i][2]);
        }

        return surrounding;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + x.hashCode();
        hashCode = 31 * hashCode + y.hashCode();
        hashCode = 31 * hashCode + z.hashCode();
        return hashCode;
    }

    public long manhattanDistance(Point3D point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z);
    }

    public double euclideanDistance(Point3D point) {
        return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2) + Math.pow(z - point.z, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point3D p) {
            return x.equals(p.x) && y.equals(p.y) && z.equals(p.z);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Point3D[x=" + x + ",y=" + y + ",z=" + z + "]";
    }

    @Override
    public Point3D clone() {
        try {
            return (Point3D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

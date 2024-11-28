package com.vuryss.aoc.util;

public class Point3D implements Cloneable {
    public Integer x;
    public Integer y;
    public Integer z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D add(Point3D delta) {
        return new Point3D(x + delta.x, y + delta.y, z + delta.z);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode += 31 * hashCode + x.hashCode();
        hashCode += 31 * hashCode + y.hashCode();
        hashCode += 31 * hashCode + z.hashCode();

        return hashCode;
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

    public long manhattanDistance(Point3D point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z);
    }
}

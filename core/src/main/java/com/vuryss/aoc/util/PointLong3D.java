package com.vuryss.aoc.util;

public class PointLong3D implements Cloneable {
    public Long x;
    public Long y;
    public Long z;

    public PointLong3D(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PointLong3D add(PointLong3D delta) {
        return new PointLong3D(x + delta.x, y + delta.y, z + delta.z);
    }

    public PointLong3D sub(PointLong3D delta) {
        return new PointLong3D(x - delta.x, y - delta.y, z - delta.z);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode += 31 * hashCode + x.hashCode();
        hashCode += 31 * hashCode + y.hashCode();
        hashCode += 31 * hashCode + z.hashCode();

        return hashCode;
    }

    public long manhattanDistance(PointLong3D point) {
        return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z);
    }

    public double euclideanDistance(PointLong3D point) {
        return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2) + Math.pow(z - point.z, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointLong3D p) {
            return x.equals(p.x) && y.equals(p.y) && z.equals(p.z);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Point3D[x=" + x + ",y=" + y + ",z=" + z + "]";
    }

    @Override
    public PointLong3D clone() {
        try {
            return (PointLong3D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

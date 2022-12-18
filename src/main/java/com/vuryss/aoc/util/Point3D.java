package com.vuryss.aoc.util;

public class Point3D {
    public Integer x;
    public Integer y;
    public Integer z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
}

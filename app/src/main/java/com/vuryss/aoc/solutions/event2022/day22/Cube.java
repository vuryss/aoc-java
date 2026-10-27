package com.vuryss.aoc.solutions.event2022.day22;

import java.util.HashMap;
import java.util.Map;

public class Cube {
    public enum Side {TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK}

    Map<Side, CubeSide> sides;

    public Cube(byte size) {
        sides = new HashMap<>() {{
            put(Side.TOP, new CubeSide(size));
            put(Side.BOTTOM, new CubeSide(size));
            put(Side.LEFT, new CubeSide(size));
            put(Side.RIGHT, new CubeSide(size));
            put(Side.FRONT, new CubeSide(size));
            put(Side.BACK, new CubeSide(size));
        }};
    }

    public boolean isMapped() {
        for (var side : sides.values()) {
            if (!side.isMapped) {
                return false;
            }
        }

        return true;
    }

    public CubeSide getSide(Side side) {
        return sides.get(side);
    }

    public void rotateUp() {
        var temp = sides.get(Side.TOP);
        sides.put(Side.TOP, sides.get(Side.BACK));
        sides.put(Side.BACK, sides.get(Side.BOTTOM));
        sides.put(Side.BOTTOM, sides.get(Side.FRONT));
        sides.put(Side.FRONT, temp);
        rotateSideClockwise(Side.LEFT);
        rotateSideAntiClockwise(Side.RIGHT);
    }

    public void rotateDown() {
        var temp = sides.get(Side.TOP);
        sides.put(Side.TOP, sides.get(Side.FRONT));
        sides.put(Side.FRONT, sides.get(Side.BOTTOM));
        sides.put(Side.BOTTOM, sides.get(Side.BACK));
        sides.put(Side.BACK, temp);
        rotateSideAntiClockwise(Side.LEFT);
        rotateSideClockwise(Side.RIGHT);
    }

    public void rotateLeft() {
        var temp = sides.get(Side.FRONT);
        sides.put(Side.FRONT, sides.get(Side.LEFT));
        rotateSideClockwise(Side.BACK);
        rotateSideClockwise(Side.BACK);
        sides.put(Side.LEFT, sides.get(Side.BACK));
        rotateSideClockwise(Side.RIGHT);
        rotateSideClockwise(Side.RIGHT);
        sides.put(Side.BACK, sides.get(Side.RIGHT));
        sides.put(Side.RIGHT, temp);
        rotateSideAntiClockwise(Side.TOP);
        rotateSideClockwise(Side.BOTTOM);
    }

    public void rotateRight() {
        var temp = sides.get(Side.FRONT);
        sides.put(Side.FRONT, sides.get(Side.RIGHT));
        rotateSideClockwise(Side.BACK);
        rotateSideClockwise(Side.BACK);
        sides.put(Side.RIGHT, sides.get(Side.BACK));
        rotateSideClockwise(Side.LEFT);
        rotateSideClockwise(Side.LEFT);
        sides.put(Side.BACK, sides.get(Side.LEFT));
        sides.put(Side.LEFT, temp);
        rotateSideClockwise(Side.TOP);
        rotateSideAntiClockwise(Side.BOTTOM);
    }

    void rotateSideClockwise(Side side) {
        var cubeSide = sides.get(side);
        var sectors = cubeSide.sectors;

        // Rotate the grid matrix clockwise
        for (var y = 0; y < sectors.length / 2; y++) {
            for (var x = y; x < sectors.length - y - 1; x++) {
                var temp = sectors[y][x];
                sectors[y][x] = sectors[sectors.length - 1 - x][y];
                sectors[sectors.length - 1 - x][y] = sectors[sectors.length - 1 - y][sectors.length - 1 - x];
                sectors[sectors.length - 1 - y][sectors.length - 1 - x] = sectors[x][sectors.length - 1 - y];
                sectors[x][sectors.length - 1 - y] = temp;
            }
        }

        if (cubeSide.recordWalkingRotations) cubeSide.rotationsDuringWalking++;
        if (!cubeSide.isMapped) cubeSide.rotationsBeforeMapping++;
    }

    void rotateSideAntiClockwise(Side side) {
        var cubeSide = sides.get(side);
        var sectors = sides.get(side).sectors;

        // Rotate the grid matrix anti-clockwise
        for (var y = 0; y < sectors.length / 2; y++) {
            for (var x = y; x < sectors.length - y - 1; x++) {
                var temp = sectors[y][x];
                sectors[y][x] = sectors[x][sectors.length - 1 - y];
                sectors[x][sectors.length - 1 - y] = sectors[sectors.length - 1 - y][sectors.length - 1 - x];
                sectors[sectors.length - 1 - y][sectors.length - 1 - x] = sectors[sectors.length - 1 - x][y];
                sectors[sectors.length - 1 - x][y] = temp;
            }
        }

        if (cubeSide.recordWalkingRotations) cubeSide.rotationsDuringWalking--;
        if (!cubeSide.isMapped) cubeSide.rotationsBeforeMapping--;
    }

    public void recordRotations() {
        for (var side : sides.values()) {
            side.recordWalkingRotations = true;
        }
    }
}

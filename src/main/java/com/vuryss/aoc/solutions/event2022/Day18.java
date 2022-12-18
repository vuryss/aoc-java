package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;
import com.vuryss.aoc.util.Point3D;

import java.util.*;

public class Day18 implements DayInterface {
    static Point3D[] cubeSideDeltas = new Point3D[] {
        new Point3D(0, 0, 1),
        new Point3D(0, 0, -1),
        new Point3D(0, 1, 0),
        new Point3D(0, -1, 0),
        new Point3D(1, 0, 0),
        new Point3D(-1, 0, 0),
    };
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1,1,1
            2,1,1
            """,
            "10",
            """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
            """,
            "64"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
            """,
            "58"
        );
    }

    @Override
    public String part1Solution(String input) {
        var cubeDefinitions = input.trim().split("\n");
        var points = new HashSet<Point3D>();

        for (var cubeDefinition: cubeDefinitions) {
            var parts = cubeDefinition.split(",");
            points.add(new Point3D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }

        var exposedSides = 0;

        for (var point: points) {
            for (var delta: cubeSideDeltas) {
                var checkedPoint = new Point3D(point.x + delta.x, point.y + delta.y, point.z + delta.z);

                if (!points.contains(checkedPoint)) {
                    exposedSides++;
                }
            }
        }

        return String.valueOf(exposedSides);
    }

    @Override
    public String part2Solution(String input) {
        var cubeDefinitions = input.trim().split("\n");
        var points = new HashSet<Point3D>();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (var cubeDefinition: cubeDefinitions) {
            var parts = cubeDefinition.split(",");
            var x = Integer.parseInt(parts[0]);
            var y = Integer.parseInt(parts[1]);
            var z = Integer.parseInt(parts[2]);

            points.add(new Point3D(x, y, z));
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }

        minX--; minY--; minZ--;
        maxX++; maxY++; maxZ++;

        var exposedSides = 0;
        var visited = new HashSet<Point3D>();
        var queue = new LinkedList<Point3D>();
        queue.add(new Point3D(minX, minY, minZ));

        while (!queue.isEmpty()) {
            var water = queue.poll();

            if (visited.contains(water)) {
                continue;
            }

            visited.add(water);

            for (var delta: cubeSideDeltas) {
                var checkedPoint = new Point3D(water.x + delta.x, water.y + delta.y, water.z + delta.z);

                if (
                    checkedPoint.x < minX
                    || checkedPoint.y < minY
                    || checkedPoint.z < minZ
                    || checkedPoint.x > maxX
                    || checkedPoint.y > maxY
                    || checkedPoint.z > maxZ
                    || visited.contains(checkedPoint)
                ) {
                    continue;
                }

                if (points.contains(checkedPoint)) {
                    exposedSides++;
                } else {
                    queue.add(checkedPoint);
                }
            }
        }

        return String.valueOf(exposedSides);
    }
}

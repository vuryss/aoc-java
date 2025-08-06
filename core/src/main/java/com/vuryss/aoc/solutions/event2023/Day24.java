package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Line2D;
import com.vuryss.aoc.util.PointLong;
import com.vuryss.aoc.util.PointLong3D;
import com.vuryss.aoc.util.StringUtil;

import java.util.*;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var hailstones = new ArrayList<Hailstone>();

        for (var line: lines) {
            var m = StringUtil.slongs(line);
            hailstones.add(
                new Hailstone(
                    new PointLong3D(m.get(0), m.get(1), m.get(2)),
                    new PointLong3D(m.get(3), m.get(4), m.get(5))
                )
            );
        }

        var count = 0;
        var min = 200000000000000L;
        var max = 400000000000000L;

        for (var i = 0; i < hailstones.size() - 1; i++) {
            for (var j = i + 1; j < hailstones.size(); j++) {
                var h1 = hailstones.get(i);
                var h2 = hailstones.get(j);
                var l1 = new Line2D(new PointLong(h1.position.x, h1.position.y), h1.velocity.x, h1.velocity.y);
                var l2 = new Line2D(new PointLong(h2.position.x, h2.position.y), h2.velocity.x, h2.velocity.y);
                var intersection = l1.intersects(l2);

                if (intersection == null) {
                    continue;
                }

                if (
                    intersection.x >= min
                    && intersection.x <= max
                    && intersection.y >= min
                    && intersection.y <= max
                    && (
                        h1.velocity.x >= 0 && intersection.x >= h1.position.x
                        || h1.velocity.x < 0 && intersection.x < h1.position.x
                    )
                    && (
                        h1.velocity.y >= 0 && intersection.y >= h1.position.y
                        || h1.velocity.y < 0 && intersection.y < h1.position.y
                    )
                    && (
                        h2.velocity.x >= 0 && intersection.x >= h2.position.x
                        || h2.velocity.x < 0 && intersection.x < h2.position.x
                    )
                    && (
                        h2.velocity.y >= 0 && intersection.y >= h2.position.y
                        || h2.velocity.y < 0 && intersection.y < h2.position.y
                    )
                ) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var hailstones = new ArrayList<Hailstone>();

        for (var line: lines) {
            var m = StringUtil.slongs(line);
            hailstones.add(
                new Hailstone(
                    new PointLong3D(m.get(0), m.get(1), m.get(2)),
                    new PointLong3D(m.get(3), m.get(4), m.get(5))
                )
            );
        }

        // For rock to hit hailstones with same (X|Y|Z) velocity, it must satisfy the following:
        // distance % (rockVelocity - hailstoneVelocity) == 0
        var possibleVelocityX = new HashSet<Integer>();
        var possibleVelocityY = new HashSet<Integer>();
        var possibleVelocityZ = new HashSet<Integer>();

        for (var i = 0; i < hailstones.size() - 1; i++) {
            for (var j = i + 1; j < hailstones.size(); j++) {
                var h1 = hailstones.get(i);
                var h2 = hailstones.get(j);

                if (h1.velocity.x.equals(h2.velocity.x)) {
                    var possible = new HashSet<Integer>();
                    var difference = h2.position.x - h1.position.x;
                    for (var v = -1000; v <= 1000; v++) {
                        if (v == h1.velocity.x) {
                            continue;
                        }

                        if (Math.floorMod(difference, v - h1.velocity.x) == 0) {
                            possible.add(v);
                        }
                    }

                    if (possibleVelocityX.isEmpty()) {
                        possibleVelocityX.addAll(possible);
                    } else {
                        possibleVelocityX.retainAll(possible);
                    }
                }

                if (h1.velocity.y.equals(h2.velocity.y)) {
                    var possible = new HashSet<Integer>();
                    var difference = h2.position.y - h1.position.y;
                    for (var v = -1000; v <= 1000; v++) {
                        if (v == h1.velocity.y) {
                            continue;
                        }

                        if (Math.floorMod(difference, v - h1.velocity.y) == 0) {
                            possible.add(v);
                        }
                    }

                    if (possibleVelocityY.isEmpty()) {
                        possibleVelocityY.addAll(possible);
                    } else {
                        possibleVelocityY.retainAll(possible);
                    }
                }

                if (h1.velocity.z.equals(h2.velocity.z)) {
                    var possible = new HashSet<Integer>();
                    var difference = h2.position.z - h1.position.z;
                    for (var v = -1000; v <= 1000; v++) {
                        if (v == h1.velocity.z) {
                            continue;
                        }

                        if (Math.floorMod(difference, v - h1.velocity.z) == 0) {
                            possible.add(v);
                        }
                    }

                    if (possibleVelocityZ.isEmpty()) {
                        possibleVelocityZ.addAll(possible);
                    } else {
                        possibleVelocityZ.retainAll(possible);
                    }
                }
            }
        }

        int rockVelocityX = possibleVelocityX.stream().findFirst().orElseThrow();
        int rockVelocityY = possibleVelocityY.stream().findFirst().orElseThrow();
        int rockVelocityZ = possibleVelocityZ.stream().findFirst().orElseThrow();

        // Pick any two hailstones and remove the rock velocity from their velocities
        var h1 = hailstones.get(0);
        var h2 = hailstones.get(1);
        var h1v = h1.velocity.sub(new PointLong3D(rockVelocityX, rockVelocityY, rockVelocityZ));
        var h2v = h2.velocity.sub(new PointLong3D(rockVelocityX, rockVelocityY, rockVelocityZ));

        // Find intersection point for just X,Y coordinates as if they were in a 2d space
        var line1 = new Line2D(new PointLong(h1.position.x, h1.position.y), h1v.x, h1v.y);
        var line2 = new Line2D(new PointLong(h2.position.x, h2.position.y), h2v.x, h2v.y);
        var intersection = line1.intersects(line2);
        assert intersection != null;

        // Now that we have X, we can calculate the time it takes for the rock to reach that X
        var time = (intersection.x - h1.position.x) / h1v.x;

        // Now we can calculate the Z coordinate for that time
        var z = h1.position.z + h1v.z * time;

        return String.valueOf(intersection.x + intersection.y + z);
    }

    private static class Hailstone {
        PointLong3D position;
        PointLong3D velocity;

        public Hailstone(PointLong3D position, PointLong3D velocity) {
            this.position = position;
            this.velocity = velocity;
        }
    }
}
package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Cuboid;
import com.vuryss.aoc.util.PointLong3D;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            pos=<0,0,0>, r=4
            pos=<1,0,0>, r=1
            pos=<4,0,0>, r=3
            pos=<0,2,0>, r=1
            pos=<0,5,0>, r=3
            pos=<0,0,3>, r=1
            pos=<1,1,1>, r=1
            pos=<1,1,2>, r=1
            pos=<1,3,1>, r=1
            """,
            "7"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            pos=<10,12,12>, r=2
            pos=<12,14,12>, r=2
            pos=<16,12,12>, r=4
            pos=<14,14,14>, r=6
            pos=<50,50,50>, r=200
            pos=<10,10,10>, r=5
            """,
            "36"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var nanoBots = input.lines()
            .map(StringUtil::sints)
            .map(longs -> new Nanobot(new PointLong3D(longs.get(0), longs.get(1), longs.get(2)), longs.get(3)))
            .toList();

        var maxRadiusNanobot = nanoBots.stream().max(Comparator.comparingInt(Nanobot::radius)).get();

        var inRange = nanoBots.stream().filter(
            nanobot -> nanobot.position.manhattanDistance(maxRadiusNanobot.position) <= maxRadiusNanobot.radius
        ).count();

        return String.valueOf(inRange);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var nanoBots = input.lines()
            .map(StringUtil::sints)
            .map(longs -> new Nanobot(new PointLong3D(longs.get(0), longs.get(1), longs.get(2)), longs.get(3)))
            .toList();
        var minPoint = new PointLong3D(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
        var maxPoint = new PointLong3D(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

        for (var nanobot : nanoBots) {
            minPoint = new PointLong3D(
                Math.min(nanobot.position.x - nanobot.radius, minPoint.x),
                Math.min(nanobot.position.y - nanobot.radius, minPoint.y),
                Math.min(nanobot.position.z - nanobot.radius, minPoint.z)
            );
            maxPoint = new PointLong3D(
                Math.max(nanobot.position.x + nanobot.radius, maxPoint.x),
                Math.max(nanobot.position.y + nanobot.radius, maxPoint.y),
                Math.max(nanobot.position.z + nanobot.radius, maxPoint.z)
            );
        }
        long maxSpan = Math.max(maxPoint.x - minPoint.x, Math.max(maxPoint.y - minPoint.y, maxPoint.z - minPoint.z));
        long powerOfTwoSize = 1L;
        while (powerOfTwoSize < maxSpan) powerOfTwoSize <<= 1;

        maxPoint = new PointLong3D(minPoint.x + powerOfTwoSize - 1, minPoint.y + powerOfTwoSize - 1, minPoint.z + powerOfTwoSize - 1);

        var center = new PointLong3D(0, 0, 0);
        var queue = new PriorityQueue<>(
            Comparator.comparingLong((Cuboid c) -> intersectingNanobots(c, nanoBots)).reversed()
                .thenComparing((Cuboid c) -> c.shortestManhattanDistance(center))
                .thenComparing(Cuboid::numberOfPoints)
        );

        queue.add(new Cuboid(minPoint, maxPoint));

        while (!queue.isEmpty()) {
            var cuboid = queue.poll();

            if (cuboid.numberOfPoints() == 1) {
                return String.valueOf(cuboid.shortestManhattanDistance(center));
            }

            long newSize = cuboid.xPoints() / 2;
            var min = cuboid.minPoint;
            var max = cuboid.maxPoint;

            // 1. Low X, Low Y, Low Z
            queue.add(new Cuboid(
                min,
                new PointLong3D(min.x + newSize - 1, min.y + newSize - 1, min.z + newSize - 1)
            ));

            // 2. High X, Low Y, Low Z
            queue.add(new Cuboid(
                new PointLong3D(min.x + newSize, min.y, min.z),
                new PointLong3D(max.x, min.y + newSize - 1, min.z + newSize - 1)
            ));

            // 3. Low X, High Y, Low Z
            queue.add(new Cuboid(
                new PointLong3D(min.x, min.y + newSize, min.z),
                new PointLong3D(min.x + newSize - 1, max.y, min.z + newSize - 1)
            ));

            // 4. High X, High Y, Low Z
            queue.add(new Cuboid(
                new PointLong3D(min.x + newSize, min.y + newSize, min.z),
                new PointLong3D(max.x, max.y, min.z + newSize - 1)
            ));

            // 5. Low X, Low Y, High Z
            queue.add(new Cuboid(
                new PointLong3D(min.x, min.y, min.z + newSize),
                new PointLong3D(min.x + newSize - 1, min.y + newSize - 1, max.z)
            ));

            // 6. High X, Low Y, High Z
            queue.add(new Cuboid(
                new PointLong3D(min.x + newSize, min.y, min.z + newSize),
                new PointLong3D(max.x, min.y + newSize - 1, max.z)
            ));

            // 7. Low X, High Y, High Z
            queue.add(new Cuboid(
                new PointLong3D(min.x, min.y + newSize, min.z + newSize),
                new PointLong3D(min.x + newSize - 1, max.y, max.z)
            ));

            // 8. High X, High Y, High Z
            queue.add(new Cuboid(
                new PointLong3D(min.x + newSize, min.y + newSize, min.z + newSize),
                max
            ));
        }

        return "-not-found-";
    }

    private long intersectingNanobots(Cuboid cuboid, List<Nanobot> nanobots) {
        long result = 0;

        for (var nanobot : nanobots) {
            if (cuboid.shortestManhattanDistance(nanobot.position) <= nanobot.radius) {
                result++;
            }
        }

        return result;
    }

    record Nanobot(PointLong3D position, int radius) {}
}

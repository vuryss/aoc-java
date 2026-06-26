package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MathUtil;
import com.vuryss.aoc.util.Point3D;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
            """,
        "183",
            """
            <x=-8, y=-10, z=0>
            <x=5, y=5, z=10>
            <x=2, y=-7, z=3>
            <x=9, y=-8, z=-3>
            """,
            "14645"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
            """,
            "2772",
            """
            <x=-8, y=-10, z=0>
            <x=5, y=5, z=10>
            <x=2, y=-7, z=3>
            <x=9, y=-8, z=-3>
            """,
            "4686774924"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var moons = new Moon[4];
        int i = 0;

        for (var line : input.trim().split("\n")) {
            var ints = StringUtil.sints(line);
            moons[i++] = new Moon(new Point3D(ints.get(0), ints.get(1), ints.get(2)), new Point3D(0, 0 ,0));
        }

        for (i = 0; i < 1000; i++) {
            for (var j = 0; j < 3; j++) {
                for (var k = j + 1; k < 4; k++) {
                    long xDiff = moons[j].position.x - moons[k].position.x;
                    long yDiff = moons[j].position.y - moons[k].position.y;
                    long zDiff = moons[j].position.z - moons[k].position.z;

                    moons[j].velocity.x += xDiff < 0 ? 1 : (xDiff == 0 ? 0 : -1);
                    moons[k].velocity.x += xDiff > 0 ? 1 : (xDiff == 0 ? 0 : -1);
                    moons[j].velocity.y += yDiff < 0 ? 1 : (yDiff == 0 ? 0 : -1);
                    moons[k].velocity.y += yDiff > 0 ? 1 : (yDiff == 0 ? 0 : -1);
                    moons[j].velocity.z += zDiff < 0 ? 1 : (zDiff == 0 ? 0 : -1);
                    moons[k].velocity.z += zDiff > 0 ? 1 : (zDiff == 0 ? 0 : -1);
                }
            }

            for (var moon : moons) moon.move();
        }

        return Arrays.stream(moons).mapToLong(Moon::energy).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var moons = new Moon[4];
        int i = 0;

        for (var line : input.trim().split("\n")) {
            var ints = StringUtil.sints(line);
            moons[i++] = new Moon(new Point3D(ints.get(0), ints.get(1), ints.get(2)), new Point3D(0, 0 ,0));
        }

        var initialX = getListX(moons);
        var initialY = getListY(moons);
        var initialZ = getListZ(moons);
        long repeatX = -1;
        long repeatY = -1;
        long repeatZ = -1;

        for (i = 1; repeatX == -1 || repeatY == -1 || repeatZ == -1; i++) {
            for (var j = 0; j < 3; j++) {
                for (var k = j + 1; k < 4; k++) {
                    long xDiff = moons[j].position.x - moons[k].position.x;
                    long yDiff = moons[j].position.y - moons[k].position.y;
                    long zDiff = moons[j].position.z - moons[k].position.z;

                    moons[j].velocity.x += xDiff < 0 ? 1 : (xDiff == 0 ? 0 : -1);
                    moons[k].velocity.x += xDiff > 0 ? 1 : (xDiff == 0 ? 0 : -1);
                    moons[j].velocity.y += yDiff < 0 ? 1 : (yDiff == 0 ? 0 : -1);
                    moons[k].velocity.y += yDiff > 0 ? 1 : (yDiff == 0 ? 0 : -1);
                    moons[j].velocity.z += zDiff < 0 ? 1 : (zDiff == 0 ? 0 : -1);
                    moons[k].velocity.z += zDiff > 0 ? 1 : (zDiff == 0 ? 0 : -1);
                }
            }

            for (var moon : moons) moon.move();

            if (repeatX == -1 && getListX(moons).equals(initialX)) {
                repeatX = i;
            }

            if (repeatY == -1 && getListY(moons).equals(initialY)) {
                repeatY = i;
            }

            if (repeatZ == -1 && getListZ(moons).equals(initialZ)) {
                repeatZ = i;
            }
        }

        return MathUtil.lcm(List.of(repeatX, repeatY, repeatZ)) + "";
    }

    private List<Long> getListX(Moon[] moons) {
        return List.of(
            moons[0].position.x, moons[1].position.x, moons[2].position.x, moons[3].position.x,
            moons[0].velocity.x, moons[1].velocity.x, moons[2].velocity.x, moons[3].velocity.x
        );
    }

    private List<Long> getListY(Moon[] moons) {
        return List.of(
            moons[0].position.y, moons[1].position.y, moons[2].position.y, moons[3].position.y,
            moons[0].velocity.y, moons[1].velocity.y, moons[2].velocity.y, moons[3].velocity.y
        );
    }

    private List<Long> getListZ(Moon[] moons) {
        return List.of(
            moons[0].position.z, moons[1].position.z, moons[2].position.z, moons[3].position.z,
            moons[0].velocity.z, moons[1].velocity.z, moons[2].velocity.z, moons[3].velocity.z
        );
    }

    private record Moon(Point3D position, Point3D velocity) {
        public long energy() {
            return (Math.abs(position.x) + Math.abs(position.y) + Math.abs(position.z))
                 * (Math.abs(velocity.x) + Math.abs(velocity.y) + Math.abs(velocity.z));
        }

        public void move() {
            var newPos = position.add(velocity);
            position.x = newPos.x;
            position.y = newPos.y;
            position.z = newPos.z;
        }
    }
}

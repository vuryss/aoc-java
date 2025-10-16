package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.*;

import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """,
            "7"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var bricks = parseBricks(input);
        var count = 0;

        for (var brick: bricks) {
            brick.removed = true;
            brick.markBricksThatWouldFall();
            count += bricks.stream().anyMatch(b -> b.wouldFall) ? 0 : 1;
            bricks.forEach(b -> { b.removed = false; b.wouldFall = false; });
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var bricks = parseBricks(input);
        var count = 0L;

        for (var brick: bricks) {
            brick.removed = true;
            brick.markBricksThatWouldFall();
            count += bricks.stream().filter(b -> b.wouldFall).count();
            bricks.forEach(b -> { b.removed = false; b.wouldFall = false; });
        }

        return String.valueOf(count);
    }

    private ArrayList<Brick> parseBricks(String input) {
        var lines = input.trim().split("\n");
        var bricks = new ArrayList<Brick>();

        for (var line: lines) {
            var m = StringUtil.ints(line);
            bricks.add(new Brick(new Point3D(m.get(0), m.get(1), m.get(2)), new Point3D(m.get(3), m.get(4), m.get(5))));
        }

        bricks.sort(Comparator.comparingInt(a -> a.a.z));

        var bricksAtLevelZ = new HashMap<Integer, ArrayList<Brick>>();

        for (var brick: bricks) {
            var minZ = brick.a.z;
            var minPossibleZ = brick.a.z;

            cycle1:
            for (var z = brick.a.z - 1; z >= 1; z--) {
                for (var b: bricksAtLevelZ.getOrDefault(z, new ArrayList<>())) {
                    if (b.a.x <= brick.b.x && b.b.x >= brick.a.x && b.a.y <= brick.b.y && b.b.y >= brick.a.y) {
                        break cycle1;
                    }
                }

                minPossibleZ = z;
            }

            brick.a.z -= minZ - minPossibleZ;
            brick.b.z -= minZ - minPossibleZ;

            for (var z = brick.a.z; z <= brick.b.z; z++) {
                if (!bricksAtLevelZ.containsKey(z)) {
                    bricksAtLevelZ.put(z, new ArrayList<>());
                }

                bricksAtLevelZ.get(z).add(brick);
            }
        }

        for (var brick: bricks) {
            // Which bricks does this brick support?
            for (var b: bricksAtLevelZ.getOrDefault(brick.b.z + 1, new ArrayList<>())) {
                if (b.a.x <= brick.b.x && b.b.x >= brick.a.x && b.a.y <= brick.b.y && b.b.y >= brick.a.y) {
                    brick.supports.add(b);
                    b.supportedBy.add(brick);
                }
            }

            // Which bricks are supported by this brick?
            for (var b: bricksAtLevelZ.getOrDefault(brick.a.z - 1, new ArrayList<>())) {
                if (b.a.x <= brick.b.x && b.b.x >= brick.a.x && b.a.y <= brick.b.y && b.b.y >= brick.a.y) {
                    brick.supportedBy.add(b);
                    b.supports.add(brick);
                }
            }
        }

        return bricks;
    }

    private static class Brick {
        public Point3D a;
        public Point3D b;
        public HashSet<Brick> supports = new HashSet<>();
        public HashSet<Brick> supportedBy = new HashSet<>();
        boolean wouldFall = false;
        boolean removed = false;

        public Brick(Point3D a, Point3D b) {
            this.a = a;
            this.b = b;
        }

        public void markBricksThatWouldFall() {
            for (var brick: supports) {
                if (brick.supportedBy.stream().noneMatch(b -> !b.removed && !b.wouldFall)) {
                    brick.wouldFall = true;
                }
            }

            supports.forEach(Brick::markBricksThatWouldFall);
        }
    }
}

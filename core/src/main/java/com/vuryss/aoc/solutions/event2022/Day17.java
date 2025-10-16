package com.vuryss.aoc.solutions.event2022;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Chars;
import com.vuryss.aoc.solutions.SolutionInterface;

import java.awt.*;
import java.util.List;
import java.util.*;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
            """,
            "3068"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
            """,
            "1514285714288"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return getMaxRockHeight(input, 2022);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return getMaxRockHeight(input, 1_000_000_000_000L);
    }

    public String getMaxRockHeight(String input, long targetRockCount) {
        var jets = Iterables.cycle(Chars.asList(input.trim().toCharArray())).iterator();
        var shapes = Iterables.cycle(Shape1.class, Shape2.class, Shape3.class, Shape4.class, Shape5.class).iterator();
        var grid = new HashSet<Point>();
        var highestY = 0;
        var heightDifferences = new ArrayList<Integer>();
        var comparedSlice = 50;
        var hashStore = new HashMap<Integer, Integer>();
        RepeatingWindow repeatingWindow;
        var recordedHeights = new HashMap<Integer, Integer>();
        var rockIndex = 0;

        while (true) {
            rockIndex++;

            Shape rock;
            int previousMaxHeight = highestY;

            try {
                rock = shapes.next().getConstructor(Point.class).newInstance(new Point(3, highestY + 4));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            while (true) {
                if (jets.next().equals('>')) {
                    rock.moveRight(grid);
                } else {
                    rock.moveLeft(grid);
                }

                if (!rock.moveDown(grid)) {
                    for (var point: rock.getPoints()) {
                        grid.add(point);
                        highestY = Math.max(point.y, highestY);
                    }

                    break;
                }
            }

            if (rockIndex == targetRockCount) {
                return String.valueOf(highestY);
            }

            recordedHeights.put(rockIndex, highestY);
            heightDifferences.add(highestY - previousMaxHeight);

            if (heightDifferences.size() > comparedSlice) {
                var hash = heightDifferences.subList(heightDifferences.size() - comparedSlice, heightDifferences.size()).hashCode();

                if (hashStore.containsKey(hash)) {
                    repeatingWindow = new RepeatingWindow(
                        hashStore.get(hash),
                        rockIndex - hashStore.get(hash),
                        highestY - recordedHeights.get(hashStore.get(hash))
                    );
                    break;
                } else {
                    hashStore.put(hash, rockIndex);
                }
            }
        }

        var rockCountFromStartOfRepeatingWindow = targetRockCount - (repeatingWindow.startRockNumber - 1);
        var repeatingWindowCount = rockCountFromStartOfRepeatingWindow / repeatingWindow.size;
        var heightBeforeRepeatingStarts = recordedHeights.get(repeatingWindow.startRockNumber - 1);
        var maxRockHeight = heightBeforeRepeatingStarts + (repeatingWindowCount * repeatingWindow.heightIncrease);
        var countedRocks = repeatingWindow.startRockNumber - 1 + (repeatingWindowCount * repeatingWindow.size);

        var recordedHeightDifferencesIndex = repeatingWindow.startRockNumber - 1;

        while (++countedRocks <= targetRockCount) {
            maxRockHeight += heightDifferences.get(recordedHeightDifferencesIndex++);
        }

        return String.valueOf(maxRockHeight);
    }

    record RepeatingWindow(
        int startRockNumber,
        int size,
        int heightIncrease
    ) {}

    static abstract class Shape {
        List<Point> points = new LinkedList<>();

        List<Point> getPoints() {
            return this.points;
        }

        void moveLeft(HashSet<Point> grid) {
            for (var point: this.points) {
                if (point.x - 1 == 0 || grid.contains(new Point(point.x - 1, point.y))) {
                    return;
                }
            }

            for (var point: this.points) {
                point.x--;
            }
        }

        void moveRight(HashSet<Point> grid) {
            for (var point: this.points) {
                if (point.x + 1 == 8 || grid.contains(new Point(point.x + 1, point.y))) {
                    return;
                }
            }

            for (var point: this.points) {
                point.x++;
            }
        }

        boolean moveDown(HashSet<Point> grid) {
            for (var point: this.points) {
                if (point.y - 1 == 0 || grid.contains(new Point(point.x, point.y - 1))) {
                    return false;
                }
            }

            for (var point: this.points) {
                point.y--;
            }

            return true;
        }
    }

    static class Shape1 extends Shape {
        List<Point> deltas = List.of(
            new Point(0,0),
            new Point(1, 0),
            new Point(2, 0),
            new Point(3, 0)
        );

        public Shape1(Point bottomLeft) {
            for (var delta: deltas) {
                points.add(new Point(bottomLeft.x + delta.x, bottomLeft.y + delta.y));
            }
        }
    }

    static class Shape2 extends Shape {
        List<Point> deltas = List.of(
            new Point(1,0),
            new Point(0, 1),
            new Point(1, 1),
            new Point(2, 1),
            new Point(1, 2)
        );

        public Shape2(Point bottomLeft) {
            for (var delta: deltas) {
                points.add(new Point(bottomLeft.x + delta.x, bottomLeft.y + delta.y));
            }
        }
    }

    static class Shape3 extends Shape {
        List<Point> deltas = List.of(
            new Point(0,0),
            new Point(1, 0),
            new Point(2, 0),
            new Point(2, 1),
            new Point(2, 2)
        );

        public Shape3(Point bottomLeft) {
            for (var delta: deltas) {
                points.add(new Point(bottomLeft.x + delta.x, bottomLeft.y + delta.y));
            }
        }
    }

    static class Shape4 extends Shape {
        List<Point> deltas = List.of(
            new Point(0,0),
            new Point(0, 1),
            new Point(0, 2),
            new Point(0, 3)
        );

        public Shape4(Point bottomLeft) {
            for (var delta: deltas) {
                points.add(new Point(bottomLeft.x + delta.x, bottomLeft.y + delta.y));
            }
        }
    }

    static class Shape5 extends Shape {
        List<Point> deltas = List.of(
            new Point(0,0),
            new Point(1, 0),
            new Point(0, 1),
            new Point(1, 1)
        );

        public Shape5(Point bottomLeft) {
            for (var delta: deltas) {
                points.add(new Point(bottomLeft.x + delta.x, bottomLeft.y + delta.y));
            }
        }
    }
}

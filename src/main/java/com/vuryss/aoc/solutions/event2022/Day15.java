package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;
import com.vuryss.aoc.Utils;
import org.apache.commons.lang3.Range;

import java.awt.*;
import java.util.*;

public class Day15 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
            """,
            "26"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
            """,
            "56000011"
        );
    }

    @Override
    public String part1Solution(String input) {
        var inputLines = input.split("\n");
        var ranges = new LinkedList<Range<Integer>>();
        var targetY = inputLines.length > 15 ? 2000000 : 10;
        var beacons = new HashSet<Point>();

        for (var line: inputLines) {
            var parts = line.split(" ");
            var sx = Utils.getNumberFromString(parts[2]);
            var sy = Utils.getNumberFromString(parts[3]);
            var bx = Utils.getNumberFromString(parts[8]);
            var by = Utils.getNumberFromString(parts[9]);
            var beaconPosition = new Point(bx, by);

            var sensor = new Sensor(new Point(sx, sy), beaconPosition);

            beacons.add(beaconPosition);

            var xDelta = sensor.manhattan - Math.abs(sensor.position.y - targetY);

            if (xDelta >= 0) {
                ranges.add(Range.between(sensor.position.x - xDelta, sensor.position.x + xDelta));
            }
        }

        var change = true;

        while (change) {
            change = false;

            outer:
            for (var i = 0; i < ranges.size() - 1; i++) {
                for (var j = i + 1; j < ranges.size(); j++) {
                    var range1 = ranges.get(i);
                    var range2 = ranges.get(j);
                    if (range1.isOverlappedBy(range2)) {
                        change = true;
                        ranges.set(
                            i,
                            Range.between(
                                Math.min(range1.getMinimum(), range2.getMinimum()),
                                Math.max(range1.getMaximum(), range2.getMaximum())
                            )
                        );
                        ranges.remove(j);
                        break outer;
                    }
                }
            }
        }

        var total = 0;

        for (var range: ranges) {
            var matchBeacons = 0;
            for (var beacon: beacons) {
                if (beacon.y == targetY && range.contains(beacon.x)) {
                    matchBeacons++;
                }
            }
            total += range.getMaximum() - range.getMinimum() + 1 - matchBeacons;
        }

        return String.valueOf(total);
    }

    @Override
    public String part2Solution(String input) {
        var inputLines = input.split("\n");
        var sensors = new ArrayList<Sensor>();
        var maxSize = inputLines.length > 15 ? 4000000 : 20;

        for (var line: inputLines) {
            var parts = line.split(" ");
            var sx = Utils.getNumberFromString(parts[2]);
            var sy = Utils.getNumberFromString(parts[3]);
            var bx = Utils.getNumberFromString(parts[8]);
            var by = Utils.getNumberFromString(parts[9]);

            sensors.add(new Sensor(new Point(sx, sy), new Point(bx, by)));
        }

        for (var sensor: sensors) {
            for (
                var x = sensor.position.x - sensor.manhattan - 1;
                x <= sensor.position.x + sensor.manhattan + 1;
                x++
            ) {
                if (x < 0 || x > maxSize) {
                    continue;
                }

                var points = new Point[]{
                    new Point(x, sensor.position.y - (sensor.manhattan + 1 - Math.abs(sensor.position.x - x))),
                    new Point(x, sensor.position.y + (sensor.manhattan + 1 - Math.abs(sensor.position.x - x)))
                };

                for (var point: points) {
                    if (point.y < 0 || point.y > maxSize) {
                        continue;
                    }

                    var match = true;

                    for (var otherSensor: sensors) {
                        if (sensor.equals(otherSensor)) {
                            continue;
                        }

                        if (Utils.manhattan(point, otherSensor.position) <= otherSensor.manhattan) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        return String.valueOf((long) point.x * 4000000L + (long) point.y);
                    }
                }
            }
        }

        return "";
    }

    public static class Sensor {
        public Point position;
        public Point beaconPosition;
        public int manhattan;

        Sensor(Point position, Point closestBeaconPosition) {
            this.position = position;
            this.beaconPosition = closestBeaconPosition;
            this.manhattan = Math.abs(position.x - beaconPosition.x) + Math.abs(position.y - beaconPosition.y);
        }
    }
}
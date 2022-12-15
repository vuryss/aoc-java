package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;
import com.vuryss.aoc.Utils;
import com.vuryss.aoc.util.RangeUtil;
import org.apache.commons.lang3.Range;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        List<Range<Integer>> ranges = new ArrayList<>();
        var targetY = inputLines.length > 15 ? 2000000 : 10;
        var beacons = new HashSet<Point>();

        for (var line: inputLines) {
            var parts = line.split(" ");
            var position = new Point(Utils.getNumberFromString(parts[2]), Utils.getNumberFromString(parts[3]));
            var beacon = new Point(Utils.getNumberFromString(parts[8]), Utils.getNumberFromString(parts[9]));
            var sensor = new Sensor(position, Utils.manhattan(position, beacon));

            beacons.add(beacon);

            var xDelta = sensor.manhattan - Math.abs(sensor.position.y - targetY);

            if (xDelta >= 0) {
                ranges.add(Range.between(sensor.position.x - xDelta, sensor.position.x + xDelta));
            }
        }

        RangeUtil.mergeOverlapping(ranges);

        var total = 0;

        for (var range: ranges) {
            for (var beacon: beacons) {
                if (beacon.y == targetY && range.contains(beacon.x)) {
                    total--;
                }
            }
            total += range.getMaximum() - range.getMinimum() + 1;
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
            var position = new Point(Utils.getNumberFromString(parts[2]), Utils.getNumberFromString(parts[3]));
            var beacon = new Point(Utils.getNumberFromString(parts[8]), Utils.getNumberFromString(parts[9]));

            sensors.add(new Sensor(position, Utils.manhattan(position, beacon) + 1));
        }

        for (var sensor: sensors) {
            for (var x = sensor.position.x - sensor.manhattan; x <= sensor.position.x + sensor.manhattan; x++) {
                if (x < 0 || x > maxSize) {
                    continue;
                }

                var points = new Point[]{
                    new Point(x, sensor.position.y - (sensor.manhattan - Math.abs(sensor.position.x - x))),
                    new Point(x, sensor.position.y + (sensor.manhattan - Math.abs(sensor.position.x - x)))
                };

                outer:
                for (var point: points) {
                    if (point.y < 0 || point.y > maxSize) {
                        continue;
                    }

                    for (var otherSensor: sensors) {
                        if (sensor.equals(otherSensor)) {
                            continue;
                        }

                        if (Utils.manhattan(point, otherSensor.position) < otherSensor.manhattan) {
                            continue outer;
                        }
                    }

                    return String.valueOf((long) point.x * 4000000L + (long) point.y);
                }
            }
        }

        return "";
    }

    record Sensor(Point position, int manhattan) {}
}
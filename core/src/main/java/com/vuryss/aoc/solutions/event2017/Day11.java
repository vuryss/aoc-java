package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.HexDirection;
import com.vuryss.aoc.util.PointHex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "ne,ne,ne", "3",
            "ne,ne,sw,sw", "0",
            "ne,ne,s,s", "2",
            "se,sw,se,sw,sw", "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var directions = Arrays.stream(input.trim().split(",")).map(HexDirection::from).toList();
        var start = new PointHex(0, 0, 0);
        var position = start;

        for (var direction: directions) {
            position = position.goInDirection(direction);
        }

        return String.valueOf(start.distanceTo(position));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var directions = Arrays.stream(input.trim().split(",")).map(HexDirection::from).toList();
        var start = new PointHex(0, 0, 0);
        var position = start;
        var max = 0L;

        for (var direction: directions) {
            position = position.goInDirection(direction);
            max = Math.max(max, start.distanceTo(position));
        }

        return String.valueOf(max);
    }
}

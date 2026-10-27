package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Reference;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            ">", "2",
            "^>v<", "4",
            "^v^v^v^v^v", "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "^v", "3",
            "^>v<", "3",
            "^v^v^v^v^v", "11"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var chars = input.trim().toCharArray();
        var map = new HashMap<Point, Integer>();
        var position = new Point(0, 0);
        map.put(position, 1);

        for (var c: chars) {
            switch (c) {
                case '^' -> position = position.add(new Point(0, 1));
                case 'v' -> position = position.add(new Point(0, -1));
                case '>' -> position = position.add(new Point(1, 0));
                case '<' -> position = position.add(new Point(-1, 0));
            }

            map.put(position, map.getOrDefault(position, 0) + 1);
        }

        return String.valueOf(map.size());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var chars = input.trim().toCharArray();
        var map = new HashMap<Point, Integer>();
        var santaPosition = new Reference<>(new Point(0, 0));
        var roboSantaPosition = new Reference<>(new Point(0, 0));
        map.put(santaPosition.value, 2);
        boolean isSanta = true;

        for (var c: chars) {
            var position = isSanta ? santaPosition : roboSantaPosition;
            isSanta = !isSanta;

            switch (c) {
                case '^' -> position.value = position.value.add(new Point(0, 1));
                case 'v' -> position.value = position.value.add(new Point(0, -1));
                case '>' -> position.value = position.value.add(new Point(1, 0));
                case '<' -> position.value = position.value.add(new Point(-1, 0));
            }

            map.put(position.value, map.getOrDefault(position.value, 0) + 1);
        }

        return String.valueOf(map.size());
    }
}

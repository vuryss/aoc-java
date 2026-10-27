package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            3,4,3,1,2
            """,
            "5934"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            3,4,3,1,2
            """,
            "26984457539"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return String.valueOf(reproduceLanternfish(input, 80));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return String.valueOf(reproduceLanternfish(input, 256));
    }

    private long reproduceLanternfish(String input, int days) {
        var lanternfish = StringUtil.ints(input);
        var map = new HashMap<Integer, Long>();

        for (var l: lanternfish) {
            map.put(l, map.getOrDefault(l, 0L) + 1);
        }

        for (var i = 0; i < days; i++) {
            var newMap = new HashMap<Integer, Long>();

            for (var entry: map.entrySet()) {
                var timeToReproduce = entry.getKey();
                var numberOfLanternfish = entry.getValue();

                if (timeToReproduce == 0) {
                    newMap.put(6, newMap.getOrDefault(6, 0L) + numberOfLanternfish);
                    newMap.put(8, newMap.getOrDefault(8, 0L) + numberOfLanternfish);
                } else {
                    newMap.put(timeToReproduce - 1, newMap.getOrDefault(timeToReproduce - 1, 0L) + numberOfLanternfish);
                }
            }

            map = newMap;
        }

        return map.values().stream().mapToLong(Long::longValue).sum();
    }
}

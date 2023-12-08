package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.CollectionUtil;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            2x3x4
            """,
            "58",
            """
            1x1x10
            """,
            "43"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            2x3x4
            """,
            "34",
            """
            1x1x10
            """,
            "14"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var total = 0;

        for (var line: lines) {
            var dims = CollectionUtil.sort(StringUtil.ints(line));
            total += 3 * dims.get(0) * dims.get(1) + 2 * dims.get(1) * dims.get(2) + 2 * dims.get(2) * dims.get(0);
        }

        return String.valueOf(total);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var total = 0;

        for (var line: lines) {
            var dims = CollectionUtil.sort(StringUtil.ints(line));
            total += 2 * dims.get(0) + 2 * dims.get(1) + dims.get(0) * dims.get(1) * dims.get(2);
        }

        return String.valueOf(total);
    }
}

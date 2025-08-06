package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day1 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
            """,
            "7"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
            """,
            "5"
        );
    }

    @Override
    public String part1Solution(String input) {
        var n = StringUtil.ints(input);
        var count = IntStream.range(1, n.size()).filter(i -> n.get(i) > n.get(i - 1)).count();

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input) {
        var n = StringUtil.ints(input);
        var count = IntStream.range(3, n.size())
            .filter(i -> n.get(i) + n.get(i - 1) + n.get(i - 2) > n.get(i - 1) + n.get(i - 2) + n.get(i - 3))
            .count();

        return String.valueOf(count);
    }
}

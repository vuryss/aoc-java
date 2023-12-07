package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.ListUtil;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1721
            979
            366
            299
            675
            1456
            """,
            "514579"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1721
            979
            366
            299
            675
            1456
            """,
            "241861950"
        );
    }

    @Override
    public String part1Solution(String input) {
        var numbers = StringUtil.uniqueIntegers(input);

        for (var n: numbers) {
            if (numbers.contains(2020 - n)) {
                return String.valueOf(n * (2020 - n));
            }
        }

        throw new RuntimeException("No solution found");
    }

    @Override
    public String part2Solution(String input) {
        var numbers = StringUtil.uniqueIntegers(input);

        for (var n1: numbers) {
            for (var n2: numbers) {
                if (!n1.equals(n2) && numbers.contains(2020 - n1 - n2)) {
                    return String.valueOf(n1 * n2 * (2020 - n1 - n2));
                }
            }
        }

        return "";
    }
}

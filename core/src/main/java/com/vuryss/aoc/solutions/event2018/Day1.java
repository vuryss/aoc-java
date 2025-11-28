package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            +1
            -2
            +3
            +1
            """,
            "3",
            """
            +1
            +1
            -2
            """,
            "0",
            """
            -1
            -2
            -3
            """,
            "-6"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            +1
            -2
            +3
            +1
            """,
            "2",
            """
            +1
            -1
            """,
            "0",
            """
            +3
            +3
            +4
            -2
            -4
            """,
            "10",
            """
            -6
            +3
            +8
            +5
            -6
            """,
            "5",
            """
            +7
            +7
            -2
            -7
            -4
            """,
            "14"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return StringUtil.sints(input.trim()).stream().mapToInt(Integer::intValue).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var numbers = StringUtil.sints(input.trim());
        var seenFrequencies = new HashSet<Integer>();
        var current = 0;
        seenFrequencies.add(current);

        while (true) {
            for (int number : numbers) {
                current += number;

                if (!seenFrequencies.add(current)) {
                    return current + "";
                }
            }
        }
    }
}

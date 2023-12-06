package com.vuryss.aoc.solutions.event2023;

import com.google.common.base.Joiner;
import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.ListUtil;

import java.util.*;

@SuppressWarnings("unused")
public class Day6 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Time:      7  15   30
            Distance:  9  40  200
            """,
            "288"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Time:      7  15   30
            Distance:  9  40  200
            """,
            "71503"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        long product = 1L;
        var times = ListUtil.extractUnsignedIntegers(lines[0]);
        var records = ListUtil.extractUnsignedIntegers(lines[1]);

        for (var i = 0; i < times.size(); i++) {
            product *= winningCombinations(times.get(i), records.get(i));
        }

        return String.valueOf(product);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var times = ListUtil.extractUnsignedIntegers(lines[0]);
        var records = ListUtil.extractUnsignedIntegers(lines[1]);
        var totalTime = Long.parseLong(Joiner.on("").join(times));
        var totalRecord = Long.parseLong(Joiner.on("").join(records));

        return String.valueOf(winningCombinations(totalTime, totalRecord));
    }

    /**
     * Problem can be solved using a quadratic equation.
     */
    private int winningCombinations(long totalTime, long currentRecord) {
        var a = -1;
        var b = totalTime;
        var c = -currentRecord;
        var sqrtDisc = Math.sqrt(b * b - 4 * a * c);
        var maxMilliseconds = Math.floor(((-b - sqrtDisc) / (2 * a)) - 0.0001);
        var minMilliseconds = Math.ceil(((-b + sqrtDisc) / (2 * a)) + 0.0001);

        return (int) (maxMilliseconds - minMilliseconds + 1);
    }
}

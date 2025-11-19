package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0
            3
            0
            1
            -3
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0
            3
            0
            1
            -3
            """,
            "10"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var numbers = StringUtil.sints(input);
        var steps = 0;
        var size = numbers.size();
        var index = 0;
        var tempIndex = 0;
        var jumpValue = 0;

        while (index >= 0 && index < size) {
            tempIndex = index;
            jumpValue = numbers.get(index);
            index += jumpValue;
            numbers.set(tempIndex, jumpValue + 1);
            steps++;
        }

        return String.valueOf(steps);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var numbers = StringUtil.sints(input);
        var steps = 0;
        var size = numbers.size();
        var index = 0;
        var tempIndex = 0;
        var jumpValue = 0;

        while (index >= 0 && index < size) {
            tempIndex = index;
            jumpValue = numbers.get(index);
            index += jumpValue;
            numbers.set(tempIndex, jumpValue >= 3 ? jumpValue - 1 : jumpValue + 1);
            steps++;
        }

        return String.valueOf(steps);
    }
}

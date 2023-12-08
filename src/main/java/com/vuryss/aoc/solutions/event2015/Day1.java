package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "(())", "0",
            "()()", "0",
            "(((", "3",
            "(()(()(", "3",
            "))(((((", "3",
            "())", "-1",
            "))(", "-1",
            ")))", "-3",
            ")())())", "-3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            ")", "1",
            "()())", "5"
        );
    }

    @Override
    public String part1Solution(String input) {
        var charCount = StringUtil.tally(input);

        return String.valueOf(charCount.getOrDefault('(', 0) - charCount.getOrDefault(')', 0));
    }

    @Override
    public String part2Solution(String input) {
        var chars = input.trim().toCharArray();
        var floor = 0;

        for (var pos = 0; pos < chars.length; pos++) {
            floor += chars[pos] == '(' ? 1 : -1;

            if (floor == -1) {
                return String.valueOf(pos + 1);
            }
        }

        throw new RuntimeException("Santa never enters the basement");
    }
}

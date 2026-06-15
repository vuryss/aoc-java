package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("12", "2", "14", "2", "1969", "654", "100756", "33583");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("14", "2", "1969", "966", "100756", "50346");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return input.trim().lines()
            .mapToLong(Long::parseLong)
            .map(l -> l / 3 - 2)
            .sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        long fuel = 0;
        var items = StringUtil.longs(input);

        for (var item: items) {
            while ((item = item / 3 - 2) > 0) fuel += item;
        }

        return fuel + "";
    }
}

package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var groups = input.trim().split("\n\n");
        var sum = 0;

        for (var group : groups) {
            sum += StringUtil.tally(group.replace("\n", "")).size();
        }

        return sum + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var groups = input.trim().split("\n\n");
        var sum = 0;

        for (var group : groups) {
            sum += Arrays.stream(group.split("\n"))
                .map(StringUtil::tally)
                .map(Map::keySet)
                .reduce((a, b) -> { a.retainAll(b); return a; })
                .get()
                .size();
        }

        return sum + "";
    }
}

package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "0,3,6", "436",
            "1,3,2", "1",
            "2,1,3", "10",
            "1,2,3", "27",
            "2,3,1", "78",
            "3,2,1", "438",
            "3,1,2", "1836"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "0,3,6", "175594",
            "1,3,2", "2578",
            "2,1,3", "3544142",
            "1,2,3", "261214",
            "2,3,1", "6895259",
            "3,2,1", "18",
            "3,1,2", "362"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, 2020) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, 30_000_000) + "";
    }

    public int solve(String input, int iterations) {
        var nums = StringUtil.ints(input);
        var seen = new int[30_000_000];

        for (var i = 0; i < nums.size(); i++) seen[nums.get(i)] = i + 1;

        int last = nums.getLast();

        for (var i = nums.size(); i < iterations; i++) {
            var previous = seen[last];
            seen[last] = i;
            last = previous == 0 ? 0 : i - previous;
        }

        return last;
    }
}

package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
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
        return input.trim().lines().map(line -> {
            var numbers = StringUtil.ints(line);
            var min = numbers.stream().mapToInt(Integer::intValue).min().orElseThrow();
            var max = numbers.stream().mapToInt(Integer::intValue).max().orElseThrow();
            return max - min;
        }).mapToInt(Integer::intValue).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return input.trim().lines().map(line -> {
            var numbers = StringUtil.ints(line);
            for (var i = 0; i < numbers.size(); i++) {
                for (var j = 0; j < numbers.size(); j++) {
                    if (i != j && numbers.get(i) % numbers.get(j) == 0) {
                        return numbers.get(i) / numbers.get(j);
                    }
                }
            }

            return 0;
        }).mapToInt(Integer::intValue).sum() + "";
    }
}

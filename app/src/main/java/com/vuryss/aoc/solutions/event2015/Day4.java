package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "abcdef", "609043",
            "pqrstuv", "1048970"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "abcdef", "6742839",
            "pqrstuv", "5714438"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var n = 0;

        while (true) {
            var hash = StringUtil.md5(input.trim() + n);

            if (hash.startsWith("00000")) {
                return String.valueOf(n);
            }

            n++;
        }
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var n = 0;

        while (true) {
            var hash = StringUtil.md5(input.trim() + n);

            if (hash.startsWith("000000")) {
                return String.valueOf(n);
            }

            n++;
        }
    }
}

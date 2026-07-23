package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1-3 a: abcde
            1-3 b: cdefg
            2-9 c: ccccccccc
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var count = 0;

        for (var line: input.trim().split("\n")) {
            var matches = Regex.matchGroups("(\\d+)-(\\d+)\\s([a-z]):\\s(\\w+)", line);
            assert matches != null;
            var tally = StringUtil.tally(matches.get(3));
            var min = Integer.parseInt(matches.get(0));
            var max = Integer.parseInt(matches.get(1));
            var occurrences = tally.getOrDefault(matches.get(2).charAt(0), 0);

            if (occurrences >= min && occurrences <= max) count++;
        }

        return count + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var count = 0;

        for (var line: input.trim().split("\n")) {
            var matches = Regex.matchGroups("(\\d+)-(\\d+)\\s([a-z]):\\s(\\w+)", line);
            assert matches != null;
            var pos1 = Integer.parseInt(matches.get(0)) - 1;
            var pos2 = Integer.parseInt(matches.get(1)) - 1;
            var ch = matches.get(2).charAt(0);
            var password = matches.get(3);

            if (password.charAt(pos1) == ch ^ password.charAt(pos2) == ch) count++;
        }

        return count + "";
    }
}

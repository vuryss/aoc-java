package com.vuryss.aoc.solutions.event2018;

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
        return Map.of(
            """
            abcdef
            bababc
            abbcde
            abcccd
            aabcdd
            abcdee
            ababab
            """,
            "12"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            abcde
            fghij
            klmno
            pqrst
            fguij
            axcye
            wvxyz
            """,
            "fgij"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        int twoCount = 0, threeCount = 0;

        for (var line: lines) {
            var tally = StringUtil.tally(line);

            if (tally.values().stream().anyMatch(i -> i == 2)) {
                twoCount++;
            }

            if (tally.values().stream().anyMatch(i -> i == 3)) {
                threeCount++;
            }
        }

        return String.valueOf(twoCount * threeCount);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");

        for (var line1: lines) {
            innerLoop:
            for (var line2: lines) {
                var diffCount = 0;
                var diffIndex = 0;

                for (var i = 0; i < line1.length(); i++) {
                    if (line1.charAt(i) != line2.charAt(i)) {
                        diffCount++;
                        diffIndex = i;
                    }

                    if (diffCount > 1) {
                        continue innerLoop;
                    }
                }

                if (diffCount == 1) {
                    return line1.substring(0, diffIndex) + line1.substring(diffIndex + 1);
                }
            }
        }

        return "-not found-";
    }
}

package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "ugknbfddgicrmopn", "1",
            "aaa", "1",
            "jchzalrnumimnmhp", "0",
            "haegwjzuvuyypxyu", "0",
            "dvszwmarrgswjxmb", "0"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "qjhvhtzxzqqjkmpb", "1",
            "xxyxx", "1",
            "uurcxstgmygtbstg", "0",
            "ieodomkazucvgmuy", "0"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var niceCount = 0;

        for (var line: lines) {
            var tally = StringUtil.tally(line);
            var vowels = tally.getOrDefault('a', 0) +
                         tally.getOrDefault('e', 0) +
                         tally.getOrDefault('i', 0) +
                         tally.getOrDefault('o', 0) +
                         tally.getOrDefault('u', 0);
            var hasDouble = Regex.matches("(\\w)\\1", line);
            var hasBad = line.contains("ab") ||
                         line.contains("cd") ||
                         line.contains("pq") ||
                         line.contains("xy");

            if (vowels >= 3 && hasDouble && !hasBad) {
                niceCount++;
            }
        }

        return String.valueOf(niceCount);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var niceCount = 0;

        for (var line: lines) {
            var hasPair = Regex.matches("(\\w\\w).*\\1", line);
            var hasRepeat = Regex.matches("(\\w)\\w\\1", line);

            if (hasPair && hasRepeat) {
                niceCount++;
            }
        }

        return String.valueOf(niceCount);
    }
}

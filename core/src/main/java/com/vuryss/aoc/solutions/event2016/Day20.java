package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.Range;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            5-8
            0-2
            4-7
            """,
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            5-8
            0-2
            4-7
            """,
            "2"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        long maxIp = isTest ? 9 : (long) Math.pow(2, 32) - 1;
        var ranges = parseRanges(input);

        nextIp:
        for (var i = 0L; i <= maxIp; i++) {
            for (var range: ranges) {
                if (range.contains(i)) {
                    i = range.getMaximum();
                    continue  nextIp;
                }
            }

            return String.valueOf(i);
        }

        return "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        long maxIp = isTest ? 9 : (long) Math.pow(2, 32) - 1;
        var ranges = parseRanges(input);
        var count = 0L;

        nextIp:
        for (var i = 0L; i <= maxIp; i++) {
            for (var range: ranges) {
                if (range.contains(i)) {
                    i = range.getMaximum();
                    continue  nextIp;
                }
            }

            count++;
        }

        return String.valueOf(count);
    }

    private ArrayList<Range<Long>> parseRanges(String input) {
        var ranges = new ArrayList<Range<Long>>();

        for (var line: input.trim().split("\n")) {
            var parts = StringUtil.longs(line);
            ranges.add(Range.of(parts.get(0), parts.get(1)));
        }

        return ranges;
    }
}

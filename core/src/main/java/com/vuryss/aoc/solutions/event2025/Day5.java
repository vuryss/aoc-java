package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.RangeUtil;
import org.apache.commons.lang3.Range;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """,
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """,
            "14"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var rangeInputs = parts[0].split("\n");
        var ids = parts[1].split("\n");
        var freshCount = 0;
        var allRanges = new ArrayList<Range<Long>>();

        for (var range: rangeInputs) {
            var p = range.split("-");
            allRanges.add(Range.of(Long.parseLong(p[0]), Long.parseLong(p[1])));
        }

        allRanges = RangeUtil.mergeOverlapping(allRanges);

        for (var id: ids) {
            var n = Long.parseLong(id);

            for (var range: allRanges) {
                if (range.contains(n)) {
                    freshCount++;
                    break;
                }
            }
        }

        return String.valueOf(freshCount);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var ranges = parts[0].split("\n");
        long freshCount = 0;
        var allRanges = new ArrayList<Range<Long>>();

        for (var range: ranges) {
            var p = range.split("-");
            allRanges.add(Range.of(Long.parseLong(p[0]), Long.parseLong(p[1])));
        }

        allRanges = RangeUtil.mergeOverlapping(allRanges);

        for (var range: allRanges) {
            freshCount += range.getMaximum() - range.getMinimum() + 1;
        }

        return String.valueOf(freshCount);
    }
}

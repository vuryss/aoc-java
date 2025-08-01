package com.vuryss.aoc.util;

import org.apache.commons.lang3.Range;

import java.util.List;

public class RangeUtil {
    public static void mergeOverlapping(List<Range<Integer>> ranges) {
        var numberOfRanges = 0;

        while (numberOfRanges != ranges.size()) {
            numberOfRanges = ranges.size();

            for (var i = 0; i < ranges.size() - 1; i++) {
                var range1 = ranges.get(i);

                for (var j = i + 1; j < ranges.size(); j++) {
                    var range2 = ranges.get(j);

                    if (range1.isOverlappedBy(range2)) {
                        ranges.remove(range1);
                        ranges.remove(range2);
                        ranges.add(RangeUtil.merge(range1, range2));
                    }
                }
            }
        }
    }

    public static Range<Integer> merge(Range<Integer> range1, Range<Integer> range2) {
        return Range.of(
            Math.min(range1.getMinimum(), range2.getMinimum()),
            Math.max(range1.getMaximum(), range2.getMaximum())
        );
    }
}

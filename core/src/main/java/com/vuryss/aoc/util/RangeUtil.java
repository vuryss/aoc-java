package com.vuryss.aoc.util;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RangeUtil {
    public static <T extends Comparable<? super T>> ArrayList<Range<T>> mergeOverlapping(List<Range<T>> ranges) {
        if (ranges.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Range<T>> sortedRanges = new ArrayList<>(ranges);

        // Sort ranges by their minimum values - O(n log n)
        sortedRanges.sort(Comparator.comparing(Range::getMinimum));

        // Merge in a single pass - O(n)
        ArrayList<Range<T>> merged = new ArrayList<>();
        Range<T> current = sortedRanges.getFirst();

        for (int i = 1; i < sortedRanges.size(); i++) {
            Range<T> next = sortedRanges.get(i);

            if (current.isOverlappedBy(next)) {
                current = merge(current, next);
            } else {
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);

        return merged;
    }

    public static <T extends Comparable<? super T>> Range<T> merge(Range<T> r1, Range<T> r2) {
        T min = r1.getMinimum().compareTo(r2.getMinimum()) <= 0 ? r1.getMinimum() : r2.getMinimum();
        T max = r1.getMaximum().compareTo(r2.getMaximum()) >= 0 ? r1.getMaximum() : r2.getMaximum();

        return Range.of(min, max);
    }
}

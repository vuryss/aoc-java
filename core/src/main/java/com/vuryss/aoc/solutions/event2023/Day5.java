package com.vuryss.aoc.solutions.event2023;

import com.google.common.collect.Range;
import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.*;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            seeds: 79 14 55 13
            
            seed-to-soil map:
            50 98 2
            52 50 48
            
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
            
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
            
            water-to-light map:
            88 18 7
            18 25 70
            
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
            
            temperature-to-humidity map:
            0 69 1
            1 0 69
            
            humidity-to-location map:
            60 56 37
            56 93 4
            """,
            "35"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            seeds: 79 14 55 13
            
            seed-to-soil map:
            50 98 2
            52 50 48
            
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
            
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
            
            water-to-light map:
            88 18 7
            18 25 70
            
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
            
            temperature-to-humidity map:
            0 69 1
            1 0 69
            
            humidity-to-location map:
            60 56 37
            56 93 4
            """,
            "46"
        );
    }

    @Override
    public String part1Solution(String input) {
        var categories = input.trim().split("\n\n");
        var seeds = StringUtil.longs(categories[0]);
        var categoriesMaps = Arrays.stream(categories)
            .skip(1)
            .map(category -> category.lines()
                .skip(1)
                .map(StringUtil::longs)
                .map(parts -> new ValueMap(parts.get(0), parts.get(1), parts.get(2)))
                .toList()
            )
            .toList();

        var min = Long.MAX_VALUE;

        for (var seed: seeds) {
            var value = seed;

            for (var categoryMaps: categoriesMaps) {
                for (var map: categoryMaps) {
                    if (value >= map.source && value <= map.source + map.count) {
                        value = map.destination + (value - map.source);
                        break;
                    }
                }
            }

            if (value < min) {
                min = value;
            }
        }

        return String.valueOf(min);
    }

    @Override
    public String part2Solution(String input) {
        var categories = input.trim().split("\n\n");
        var seeds = StringUtil.longs(categories[0]);
        var categoriesMaps = Arrays.stream(categories)
            .skip(1)
            .map(category -> category.lines()
                .skip(1)
                .map(StringUtil::longs)
                .map(parts -> new ValueMap(parts.get(0), parts.get(1), parts.get(2)))
                .toList()
            )
            .toList();

        var min = Long.MAX_VALUE;

        for (var i = 0; i < seeds.size(); i += 2) {
            var seedRange = Range.closed(seeds.get(i), seeds.get(i) + seeds.get(i + 1) - 1);

            var valueRanges = List.of(seedRange);

            for (var categoryMaps: categoriesMaps) {
                valueRanges = applyCategoryMaps(valueRanges, categoryMaps);
            }

            for (var valueRange: valueRanges) {
                if (valueRange.lowerEndpoint() < min) {
                    min = valueRange.lowerEndpoint();
                }
            }
        }

        return String.valueOf(min);
    }

    private ArrayList<Range<Long>> applyCategoryMaps(List<Range<Long>> ranges, List<ValueMap> categoryMaps) {
        var unmappedRanges = List.copyOf(ranges);
        var mappedRanges = new ArrayList<Range<Long>>();

        for (var map: categoryMaps) {
            var newUnmappedRanges = new ArrayList<Range<Long>>();

            for (var unmappedRange: unmappedRanges) {
                var mapRange = Range.closed(map.source, map.source + map.count - 1);

                if (!unmappedRange.isConnected(mapRange)) {
                    newUnmappedRanges.add(unmappedRange);
                    continue;
                }

                var overlapping = unmappedRange.intersection(mapRange);

                // Overlapping range is mapped to the destination range.
                mappedRanges.add(Range.closed(
                    map.destination + (overlapping.lowerEndpoint() - map.source),
                    map.destination + (overlapping.upperEndpoint() - map.source)
                ));

                // If there is a range before the overlapping range, add it to the unmapped ranges.
                if (unmappedRange.lowerEndpoint() < overlapping.lowerEndpoint()) {
                    newUnmappedRanges.add(Range.closed(unmappedRange.lowerEndpoint(), overlapping.lowerEndpoint() - 1));
                }

                // If there is a range after the overlapping range, add it to the unmapped ranges.
                if (unmappedRange.upperEndpoint() > overlapping.upperEndpoint()) {
                    newUnmappedRanges.add(Range.closed(overlapping.upperEndpoint() + 1, unmappedRange.upperEndpoint()));
                }
            }

            unmappedRanges = newUnmappedRanges;
        }

        mappedRanges.addAll(unmappedRanges);

        return mappedRanges;
    }

    record ValueMap(long destination, long source, long count) { }
}

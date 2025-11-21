package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "3, 4, 1, 5", "12"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lengths = StringUtil.ints(input);
        var list = new ArrayList<>(
            isTest ? List.of(0, 1, 2, 3, 4) : IntStream.range(0, 256).boxed().collect(Collectors.toList())
        );
        var position = 0;
        var skipSize = 0;

        for (var length: lengths) {
            Collections.reverse(list.subList(0, length));
            Collections.rotate(list, -length - skipSize);
            position += length + skipSize;
            skipSize++;
        }

        Collections.rotate(list, position % list.size());

        return String.valueOf(list.get(0) * list.get(1));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return new KnotHash(input).hexForm();
    }
}

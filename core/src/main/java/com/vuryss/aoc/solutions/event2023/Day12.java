package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """,
            "21"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """,
            "525152"
        );
    }

    private final HashMap<Triple<String, List<Integer>, Boolean>, Long> memory = new HashMap<>();

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var sum = 0L;

        for (var line: lines) {
            var parts = line.split(" ");
            var springsLine = parts[0].chars().mapToObj(Character::toString).collect(Collectors.joining());
            var numbers = StringUtil.ints(parts[1]);

            sum += combinationsCount(springsLine, numbers, false);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var sum = 0L;

        for (var line: lines) {
            var parts = line.split(" ");
            var springsLine = parts[0].chars().mapToObj(Character::toString).collect(Collectors.joining());
            var numbers = StringUtil.ints(parts[1]);
            var newSpringsLine = springsLine + ('?' + springsLine).repeat(4);
            var newNumbers = new LinkedList<>(numbers){{
                addAll(numbers); addAll(numbers); addAll(numbers); addAll(numbers);
            }};

            sum += combinationsCount(newSpringsLine, newNumbers, false);
        }

        return String.valueOf(sum);
    }

    private long combinationsCount(String line, List<Integer> damaged, boolean inGroup) {
        var state = Triple.of(line, damaged, inGroup);
        var cached = cacheRead(state);

        if (cached != null) {
            return cached;
        }

        if (line.isEmpty()) {
            return damaged.isEmpty() || (damaged.size() == 1 && damaged.getFirst() == 0) ? 1 : 0;
        }

        var head = line.charAt(0);
        var remainingLine = line.substring(1);

        switch (head) {
            case '.' -> {
                if (inGroup) {
                    if (damaged.getFirst() != 0) {
                        return 0;
                    }

                    return cache(state, combinationsCount(remainingLine, damaged.subList(1, damaged.size()), false));
                }

                return cache(state, combinationsCount(remainingLine, damaged, false));
            }
            case '#' -> {
                if (!damaged.isEmpty() && damaged.getFirst() > 0) {
                    var reducedDamaged = new LinkedList<>(damaged){{ set(0, damaged.getFirst() - 1); }};

                    return cache(state, combinationsCount(remainingLine, reducedDamaged, true));
                }
            }
            case '?' -> {
                return cache(
                    state,
                    combinationsCount('.' + remainingLine, damaged, inGroup)
                    + combinationsCount('#' + remainingLine, damaged, inGroup)
                );
            }
        }

        return 0;
    }

    private long cache(Triple<String, List<Integer>, Boolean> state, long result) {
        memory.put(state, result);
        return result;
    }

    public Long cacheRead(Triple<String, List<Integer>, Boolean> state) {
        return memory.getOrDefault(state, null);
    }
}
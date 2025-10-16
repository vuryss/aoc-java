package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    private int minGroupSize = Integer.MAX_VALUE;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1
            2
            3
            4
            5
            7
            8
            9
            10
            11
            """,
            "99"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1
            2
            3
            4
            5
            7
            8
            9
            10
            11
            """,
            "44"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solveForGroups(input, 3);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solveForGroups(input, 4);
    }

    private String solveForGroups(String input, int groups) {
        var numbers = StringUtil.ints(input);
        var targetWeight = numbers.stream().mapToInt(Integer::intValue).sum() / groups;
        Set<Set<Integer>> combinations = new HashSet<>();
        minGroupSize = Integer.MAX_VALUE;

        numbers.sort(Comparator.comparingInt(Integer::intValue).reversed());
        combinations(numbers, combinations, Set.of(), targetWeight);
        combinations.removeIf(combination -> combination.size() > minGroupSize);

        var minQE = combinations
            .stream()
            .mapToLong(c -> c.stream().mapToLong(Integer::longValue).reduce(1, (a, b) -> a * b))
            .min()
            .orElseThrow();

        return String.valueOf(minQE);
    }

    private void combinations(List<Integer> numbers, Set<Set<Integer>> combinations, Set<Integer> combination, int targetWeight) {
        for (var i = 0; i < numbers.size(); i++) {
            var number = numbers.get(i);
            var newCombination = new HashSet<>(combination);
            newCombination.add(number);

            if (number == targetWeight) {
                combinations.add(newCombination);
                minGroupSize = Math.min(minGroupSize, newCombination.size());
                break;
            }

            if (number < targetWeight && combination.size() < minGroupSize - 1) {
                combinations(numbers.subList(i + 1, numbers.size()), combinations, newCombination, targetWeight - number);
            }
        }
    }
}

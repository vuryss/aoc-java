package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            20
            15
            10
            5
            5
            """,
            "4"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            20
            15
            10
            5
            5
            """,
            "3"
        );
    }

    @Override
    public String part1Solution(String input) {
        var containers = StringUtil.ints(input.trim());
        var liters = containers.size() > 5 ? 150 : 25;

        return String.valueOf(calculateCombinations(containers, liters).size());
    }

    @Override
    public String part2Solution(String input) {
        var containers = StringUtil.ints(input.trim());
        var liters = containers.size() > 5 ? 150 : 25;
        var combinations = calculateCombinations(containers, liters);
        var minContainers = combinations.stream().mapToInt(List::size).min().orElseThrow();
        var minCombinations = combinations.stream().filter(c -> c.size() == minContainers).count();

        return String.valueOf(minCombinations);
    }

    private ArrayList<ArrayList<Integer>> calculateCombinations(List<Integer> containers, int liters) {
        var combinations = new ArrayList<ArrayList<Integer>>();

        while (!containers.isEmpty()) {
            var container = containers.removeFirst();

            if (container == liters) {
                combinations.add(new ArrayList<>(List.of(container)));
                continue;
            }

            if (container < liters) {
                for (var combination: calculateCombinations(new ArrayList<>(containers), liters - container)) {
                    combination.add(container);
                    combinations.add(combination);
                }
            }
        }

        return combinations;
    }
}

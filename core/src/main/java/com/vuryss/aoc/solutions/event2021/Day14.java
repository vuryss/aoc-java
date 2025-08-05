package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.DayInterface;
import org.apache.commons.lang3.tuple.Pair;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day14 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            NNCB
            
            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
            """,
            "1588"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            NNCB
            
            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
            """,
            "2188189693529"
        );
    }

    @Override
    public String part1Solution(String input) {
        return solveForSteps(input, 10);
    }

    @Override
    public String part2Solution(String input) {
        return solveForSteps(input, 40);
    }

    private String solveForSteps(String input, int steps) {
        var parts = input.split("\n\n");
        var template = parts[0];
        var rules = new HashMap<String, Pair<String, String>>();
        var state = new HashMap<String, Long>();

        for (var i = 0; i < template.length() - 1; i++) {
            var pair = template.substring(i, i + 2);
            state.put(pair, state.getOrDefault(pair, 0L) + 1);
        }

        for (var line: parts[1].split("\n")) {
            var map = line.split(" -> ");
            rules.put(map[0], Pair.of(map[0].charAt(0) + map[1], map[1] + map[0].charAt(1)));
        }

        for (var step = 0; step < steps; step++) {
            var newState = new HashMap<String, Long>();

            for (var entry: state.entrySet()) {
                var pair = entry.getKey();
                var count = entry.getValue();
                var newPairs = rules.get(pair);

                newState.put(newPairs.getLeft(), newState.getOrDefault(newPairs.getLeft(), 0L) + count);
                newState.put(newPairs.getRight(), newState.getOrDefault(newPairs.getRight(), 0L) + count);
            }

            state = newState;
        }

        var letterCounts = new HashMap<Character, Long>();

        for (var entry: state.entrySet()) {
            var pair = entry.getKey();
            var count = entry.getValue();

            letterCounts.put(pair.charAt(0), letterCounts.getOrDefault(pair.charAt(0), 0L) + count);
            letterCounts.put(pair.charAt(1), letterCounts.getOrDefault(pair.charAt(1), 0L) + count);
        }

        var min = Long.MAX_VALUE;
        var max = Long.MIN_VALUE;

        for (var entry: letterCounts.entrySet()) {
            min = Math.min(min, (long) Math.ceil((double) entry.getValue() / 2));
            max = Math.max(max, (long) Math.ceil((double) entry.getValue() / 2));
        }

        return String.valueOf(max - min);
    }
}

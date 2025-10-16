package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    public enum COMPOUND {
        CHILDREN,
        CATS,
        SAMOYEDS,
        POMERANIANS,
        AKITAS,
        VIZSLAS,
        GOLDFISH,
        TREES,
        CARS,
        PERFUMES
    }

    public final Map<COMPOUND, Integer> expected = Map.of(
        COMPOUND.CHILDREN, 3,
        COMPOUND.CATS, 7,
        COMPOUND.SAMOYEDS, 2,
        COMPOUND.POMERANIANS, 3,
        COMPOUND.AKITAS, 0,
        COMPOUND.VIZSLAS, 0,
        COMPOUND.GOLDFISH, 5,
        COMPOUND.TREES, 3,
        COMPOUND.CARS, 2,
        COMPOUND.PERFUMES, 1
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var aunts = parseAunts(input);

        for (var aunt: aunts.entrySet()) {
            if (aunt.getValue().entrySet().stream().allMatch(e -> expected.get(e.getKey()).equals(e.getValue()))) {
                return String.valueOf(aunt.getKey());
            }
        }

        return "-not-found-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var aunts = parseAunts(input);

        for (var aunt: aunts.entrySet()) {
            var match = aunt.getValue().entrySet().stream().allMatch(e -> {
                var value = e.getValue();
                var expectedAmount = expected.get(e.getKey());

                return switch (e.getKey()) {
                    case CATS, TREES -> value > expectedAmount;
                    case POMERANIANS, GOLDFISH -> value < expectedAmount;
                    default -> value.equals(expectedAmount);
                };
            });

            if (match) {
                return String.valueOf(aunt.getKey());
            }
        }

        return "-not-found-";
    }

    private HashMap<Integer, Map<COMPOUND, Integer>> parseAunts(String input) {
        var aunts = new HashMap<Integer, Map<COMPOUND, Integer>>();

        for (var line: input.trim().split("\n")) {
            var matches = Regex.matchGroups("Sue (\\d+): (.*)", line);
            assert matches != null;

            var aunt = new HashMap<COMPOUND, Integer>();
            aunts.put(Integer.parseInt(matches.get(0)), aunt);
            var compounds = matches.get(1).split(", ");

            for (var compound: compounds) {
                var parts = Regex.matchGroups("(\\w+): (\\d+)", compound);
                assert parts != null;
                aunt.put(COMPOUND.valueOf(parts.get(0).toUpperCase()), Integer.parseInt(parts.get(1)));
            }
        }

        return aunts;
    }
}

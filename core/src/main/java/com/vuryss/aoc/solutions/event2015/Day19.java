package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            H => HO
            H => OH
            O => HH
            
            HOH
            """,
            "4",
            """
            H => HO
            H => OH
            O => HH
            
            HOHOHO
            """,
            "7"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            e => H
            e => O
            H => HO
            H => OH
            O => HH
            
            HOH
            """,
            "3",
            """
            e => H
            e => O
            H => HO
            H => OH
            O => HH
            
            HOHOHO
            """,
            "6"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var replacements = new HashMap<String, List<String>>();
        var molecule = parts[1];
        var molecules = new HashSet<String>();

        for (var line: parts[0].split("\n")) {
            var parts2 = line.split(" => ");
            replacements.computeIfAbsent(parts2[0], k -> new ArrayList<>()).add(parts2[1]);
        }

        for (var i = 0; i < molecule.length(); i++) {
            for (var entry: replacements.entrySet()) {
                for (var replacement: entry.getValue()) {
                    if (molecule.startsWith(entry.getKey(), i)) {
                        molecules.add(molecule.substring(0, i) + replacement + molecule.substring(i + entry.getKey().length()));
                    }
                }
            }
        }

        return String.valueOf(molecules.size());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var comparator = Comparator.comparingInt(String::length).reversed().thenComparing(Comparator.naturalOrder());
        var reverseReplacements = new TreeMap<String, String>(comparator);
        var molecule = parts[1];

        for (var line: parts[0].split("\n")) {
            var parts2 = line.split(" => ");
            reverseReplacements.put(parts2[1], parts2[0]);
        }

        var queue = new TreeSet<State>(Comparator.comparingInt(a -> a.molecule.length()));
        queue.add(new State(molecule, 0));

        while (!queue.isEmpty()) {
            var state = Objects.requireNonNull(queue.pollFirst());

            if (state.molecule.equals("e")) {
                return String.valueOf(state.steps);
            }

            for (var entry: reverseReplacements.entrySet()) {
                if (state.molecule.contains(entry.getKey())) {
                    queue.add(new State(state.molecule.replaceFirst(entry.getKey(), entry.getValue()), state.steps + 1));
                }
            }
        }

        return "";
    }

    record State(String molecule, int steps) {}
}

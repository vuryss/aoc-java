package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            pbga (66)
            xhth (57)
            ebii (61)
            havc (66)
            ktlj (57)
            fwft (72) -> ktlj, cntj, xhth
            qoyq (66)
            padx (45) -> pbga, havc, qoyq
            tknk (41) -> ugml, padx, fwft
            jptl (61)
            ugml (68) -> gyxo, ebii, jptl
            gyxo (61)
            cntj (57)
            """,
            "tknk"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            pbga (66)
            xhth (57)
            ebii (61)
            havc (66)
            ktlj (57)
            fwft (72) -> ktlj, cntj, xhth
            qoyq (66)
            padx (45) -> pbga, havc, qoyq
            tknk (41) -> ugml, padx, fwft
            jptl (61)
            ugml (68) -> gyxo, ebii, jptl
            gyxo (61)
            cntj (57)
            """,
            "60"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var programs = parsePrograms(input);
        var root = programs.values().stream().filter(p -> p.parent == null).findFirst().orElseThrow();

        return root.name;
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var programs = parsePrograms(input);
        var root = programs.values().stream().filter(p -> p.parent == null).findFirst().orElseThrow();

        return String.valueOf(findCorrectBalanceWeight(root));
    }

    private HashMap<String, Program> parsePrograms(String input) {
        var lines = input.trim().split("\n");
        var programs = new HashMap<String, Program>();
        var programLinks = new HashMap<String, ArrayList<String>>();

        for (var line: lines) {
            var parts = Regex.matchAll("\\w+", line);
            programs.put(parts.getFirst(), new Program(parts.getFirst(), Integer.parseInt(parts.get(1))));
            programLinks.put(parts.getFirst(), new ArrayList<>(parts.subList(2, parts.size())));
        }

        for (var entry: programLinks.entrySet()) {
            for (var child: entry.getValue()) {
                programs.get(entry.getKey()).children.add(programs.get(child));
                programs.get(child).parent = programs.get(entry.getKey());
            }
        }

        return programs;
    }

    private Integer findCorrectBalanceWeight(Program program) {
        for (var child: program.children) {
            if (!child.isBalanced()) {
                return findCorrectBalanceWeight(child);
            }
        }

        var weightCounts = new HashMap<Integer, Integer>();
        var weightProgram = new HashMap<Integer, Program>();

        for (var child: program.children) {
            weightCounts.put(child.getWeight(), weightCounts.getOrDefault(child.getWeight(), 0) + 1);
            weightProgram.put(child.getWeight(), child);
        }

        var unbalancedWeight = Integer.MIN_VALUE;
        var balancedWeight = Integer.MIN_VALUE;

        for (var entrySet: weightCounts.entrySet()) {
            if (entrySet.getValue() == 1) {
                unbalancedWeight = entrySet.getKey();
            } else {
                balancedWeight = entrySet.getKey();
            }
        }

        return weightProgram.get(unbalancedWeight).weight + (balancedWeight - unbalancedWeight);
    }

    private static class Program {
        public String name;
        public int weight;
        public ArrayList<Program> children = new ArrayList<>();
        public Program parent = null;
        private int cachedWeight = -1;

        public Program(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public int getWeight() {
            if (cachedWeight == -1) {
                cachedWeight = weight;

                for (var child: children) {
                    cachedWeight += child.getWeight();
                }
            }

            return cachedWeight;
        }

        public boolean isBalanced() {
            return children.stream().mapToInt(Program::getWeight).distinct().count() == 1;
        }
    }
}

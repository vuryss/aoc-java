package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    private final HashMap<String, Bag> bags = new HashMap<>();

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            light red bags contain 1 bright white bag, 2 muted yellow bags.
            dark orange bags contain 3 bright white bags, 4 muted yellow bags.
            bright white bags contain 1 shiny gold bag.
            muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
            shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
            dark olive bags contain 3 faded blue bags, 4 dotted black bags.
            vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
            faded blue bags contain no other bags.
            dotted black bags contain no other bags.
            """,
            "4"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
           light red bags contain 1 bright white bag, 2 muted yellow bags.
           dark orange bags contain 3 bright white bags, 4 muted yellow bags.
           bright white bags contain 1 shiny gold bag.
           muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
           shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
           dark olive bags contain 3 faded blue bags, 4 dotted black bags.
           vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
           faded blue bags contain no other bags.
           dotted black bags contain no other bags.
           """,
            "32",
            """
            shiny gold bags contain 2 dark red bags.
            dark red bags contain 2 dark orange bags.
            dark orange bags contain 2 dark yellow bags.
            dark yellow bags contain 2 dark green bags.
            dark green bags contain 2 dark blue bags.
            dark blue bags contain 2 dark violet bags.
            dark violet bags contain no other bags.
           """,
            "126"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        constructBagsGraph(input);

        var parentContainers = new HashSet<String>();
        findParentContainers(getBag("shiny gold"), parentContainers);

        return parentContainers.size() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        constructBagsGraph(input);

        return countBags(getBag("shiny gold")) + "";
    }

    private void constructBagsGraph(String input) {
        bags.clear();

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" bags contain ");
            var bagName = parts[0].trim();
            var bag = getBag(bagName);

            if (parts[1].trim().equals("no other bags.")) continue;
            var containsInputs = parts[1].split(", ");

            for (var containsInput: containsInputs) {
                var matches = Regex.matchGroups("(\\d+)\\s(.+)\\sbags?(?:\\.|$)", containsInput);
                var containsBag = getBag(matches.get(1));
                var count = Integer.parseInt(matches.get(0));

                bag.contains.put(containsBag.name, count);
                containsBag.containedIn.add(bag.name);
            }
        }
    }

    private void findParentContainers(Bag current, Set<String> found) {
        for (var parent: current.containedIn) {
            found.add(parent);
            findParentContainers(getBag(parent), found);
        }
    }

    private int countBags(Bag bag) {
        var count = 0;

        for (var es: bag.contains.entrySet()) count += es.getValue() + es.getValue() * countBags(getBag(es.getKey()));

        return count;
    }

    private Bag getBag(String bagName) {
        return bags.computeIfAbsent(bagName, name -> new Bag(name, new HashMap<>(), new HashSet<>()));
    }

    private record Bag(String name, Map<String, Integer> contains, Set<String> containedIn) {}
}

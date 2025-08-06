package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
        """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
            """,
            "157"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
            """,
            "70"
        );
    }

    @Override
    public String part1Solution(String input) {
        var rucksacksContents = input.trim().split("\n");
        int sum = 0;

        for (var rucksackContent: rucksacksContents) {
            int compartmentSize = rucksackContent.length() / 2;
            var first = rucksackContent.substring(0, compartmentSize).chars().mapToObj(i -> (char) i).toList();
            var second = rucksackContent.substring(compartmentSize).chars().mapToObj(i -> (char) i).toList();

            sum += first.stream().filter(second::contains).distinct().mapToInt(this::charValue).sum();
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var rucksacksContents = new LinkedList<>(Arrays.asList(input.trim().split("\n")));
        var sum = 0;

        while (!rucksacksContents.isEmpty()) {
            var first = rucksacksContents.poll().chars().mapToObj(a -> (char) a).toList();
            var second = rucksacksContents.poll().chars().mapToObj(a -> (char) a).toList();
            var third = rucksacksContents.poll().chars().mapToObj(a -> (char) a).toList();

            sum += first.stream().filter(second::contains).filter(third::contains)
                .mapToInt(this::charValue).findAny().getAsInt();
        }

        return String.valueOf(sum);
    }

    public int charValue(char c) {
        return c >= 97 ? c - 96 : c - 38;
    }
}

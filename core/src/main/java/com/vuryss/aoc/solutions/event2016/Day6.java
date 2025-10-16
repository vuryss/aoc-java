package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            eedadn
            drvtee
            eandsr
            raavrd
            atevrs
            tsrnev
            sdttsa
            rasrtv
            nssdts
            ntnada
            svetve
            tesnvt
            vntsnd
            vrdear
            dvrsen
            enarar
            """,
            "easter"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            eedadn
            drvtee
            eandsr
            raavrd
            atevrs
            tsrnev
            sdttsa
            rasrtv
            nssdts
            ntnada
            svetve
            tesnvt
            vntsnd
            vrdear
            dvrsen
            enarar
            """,
            "advent"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var occurrences = getCharacterOccurrencesPerPosition(input);

        return occurrences.values().stream()
            .map(m -> m.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey())
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var occurrences = getCharacterOccurrencesPerPosition(input);

        return occurrences.values().stream()
            .map(m -> m.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey())
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private LinkedHashMap<Integer, Map<Character, Integer>> getCharacterOccurrencesPerPosition(String input) {
        var lines = input.trim().split("\n");
        var occurrences = new LinkedHashMap<Integer, Map<Character, Integer>>();

        for (var line: lines) {
            for (var i = 0; i < line.length(); i++) {
                var c = line.charAt(i);
                occurrences.computeIfAbsent(i, k -> new HashMap<>());
                occurrences.get(i).put(c, occurrences.get(i).getOrDefault(c, 0) + 1);
            }
        }

        return occurrences;
    }
}

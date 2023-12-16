package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;

@SuppressWarnings("unused")
public class Day15 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            HASH
            """,
            "52",
            """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
            """,
            "1320"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
            """,
            "145"
        );
    }

    @Override
    public String part1Solution(String input) {
        var sum = 0;
        var steps = input.trim().split(",");

        for (var step: steps) {
            sum += step.chars().reduce(0, (a, b) -> (a + b) * 17 % 256);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var sum = 0;
        var steps = input.trim().split(",");
        var hashmap = new HashMap<Integer, LinkedHashMap<String, Integer>>();

        for (var step: steps) {
            var matches = Regex.matchNamedGroups("(?<name>[a-z]+)(?<sign>[=\\-])(?<value>\\d+)?", step);
            var lensName = matches.get("name");
            var hash = lensName.chars().reduce(0, (a, b) -> (a + b) * 17 % 256);
            var list = hashmap.computeIfAbsent(hash, h -> new LinkedHashMap<>());

            switch (matches.get("sign").charAt(0)) {
                case '=' -> list.put(lensName, Integer.parseInt(matches.get("value")));
                case '-' -> list.remove(lensName);
            }
        }

        for (var mapEntry: hashmap.entrySet()) {
            var lensIndex = 1;

            for (var lensValue: mapEntry.getValue().values()) {
                sum += (1 + mapEntry.getKey()) * lensIndex++ * lensValue;
            }
        }

        return String.valueOf(sum);
    }
}

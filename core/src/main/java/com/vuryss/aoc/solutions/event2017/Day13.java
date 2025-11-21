package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0: 3
            1: 2
            4: 4
            6: 4
            """,
            "24"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0: 3
            1: 2
            4: 4
            6: 4
            """,
            "10"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var firewalls = parseFirewall(input);
        var intervals = new HashMap<Integer, Integer>();
        firewalls.forEach((k, v) -> intervals.put(k, v * 2 - 2));

        int maxPicosecond = firewalls.keySet().stream().max(Integer::compareTo).orElseThrow();
        var severity = 0;

        for (var es: firewalls.entrySet()) {
            if (es.getKey() % intervals.get(es.getKey()) == 0) {
                severity += es.getKey() * es.getValue();
            }
        }

        return String.valueOf(severity);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var firewall = parseFirewall(input);
        var intervals = new HashMap<Integer, Integer>();
        firewall.forEach((k, v) -> intervals.put(k, v * 2 - 2));

        loop:
        for (var picosecond = 0; true; picosecond++) {
            for (var es: intervals.entrySet()) {
                if ((picosecond + es.getKey()) % es.getValue() == 0) {
                    continue loop;
                }
            }

            return String.valueOf(picosecond);
        }
    }

    private HashMap<Integer, Integer> parseFirewall(String input) {
        var firewall = new HashMap<Integer, Integer>();

        for (var line: input.trim().split("\n")) {
            var parts = StringUtil.ints(line);
            firewall.put(parts.getFirst(), parts.get(1));
        }

        return firewall;
    }
}

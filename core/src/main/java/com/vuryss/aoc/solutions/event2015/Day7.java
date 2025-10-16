package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> i
            543 -> a
            """,
            "543"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> i
            543 -> a
            887 -> b
            """,
            "543"
        );
    }

    private static final Map<String, WireOperation> operationMap = new LinkedHashMap<>(){{
        put("NOT (\\w+) -> (\\w+)", WireOperation.NOT);
        put("(\\w+) AND (\\w+) -> (\\w+)", WireOperation.AND);
        put("(\\w+) OR (\\w+) -> (\\w+)", WireOperation.OR);
        put("(\\w+) LSHIFT (\\w+) -> (\\w+)", WireOperation.LSHIFT);
        put("(\\w+) RSHIFT (\\w+) -> (\\w+)", WireOperation.RSHIFT);
        put("(\\w+) -> (\\w+)", WireOperation.ASSIGN);
    }};

    @Override
    public String part1Solution(String input, boolean isTest) {
        return String.valueOf(
            assembleWires(input).get("a").getValue()
        );
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var wires = assembleWires(input);
        var aValue = wires.get("a").getValue();

        wires.values().forEach(wire -> wire.value = null);
        wires.get("b").value = aValue;

        return String.valueOf(wires.get("a").getValue());
    }

    private HashMap<String, Wire> assembleWires(String input) {
        var lines = input.trim().split("\n");
        var wires = new HashMap<String, Wire>();

        lineIteration:
        for (var line: lines) {
            for (var es: operationMap.entrySet()) {
                var matches = Regex.matchGroups(es.getKey(), line);

                if (matches != null) {
                    var wire = new Wire(matches.removeLast(), es.getValue(), matches, null);
                    wires.put(wire.name, wire);
                    continue lineIteration;
                }
            }
        }

        for (var wire: wires.values()) {
            for (var i = 0; i < wire.inputs.length; i++) {
                if (wire.inputs[i] != null && wires.containsKey(wire.inputs[i].name)) {
                    wire.inputs[i] = wires.get(wire.inputs[i].name);
                }
            }
        }

        return wires;
    }

    enum WireOperation { AND, OR, LSHIFT, RSHIFT, NOT, ASSIGN }

    private static class Wire {
        public String name;
        public WireOperation operation;
        public Integer value;
        public Wire[] inputs = new Wire[2];

        public Wire(String name, WireOperation operation, List<String> inputs, Integer value) {
            this.name = name;
            this.operation = operation;
            this.value = value;

            for (var i = 0; i < inputs.size(); i++) {
                var input = inputs.get(i);
                this.inputs[i] = new Wire(
                    input,
                    WireOperation.ASSIGN,
                    List.of(),
                    StringUtils.isNumeric(input) ? Integer.parseInt(input) : null
                );
            }
        }

        public Integer getValue() {
            if (value != null) {
                return value;
            }

            return value = switch (operation) {
                case AND -> (inputs[0].getValue() & inputs[1].getValue()) & 65535;
                case OR -> (inputs[0].getValue() | inputs[1].getValue()) & 65535;
                case LSHIFT -> (inputs[0].getValue() << inputs[1].getValue()) & 65535;
                case RSHIFT -> (inputs[0].getValue() >> inputs[1].getValue()) & 65535;
                case NOT -> 65535 ^ inputs[0].getValue();
                case ASSIGN -> inputs[0].getValue();
            };
        }
    }
}

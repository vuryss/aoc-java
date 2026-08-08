package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6
            """,
            "8"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var instructions = parseInput(input);
        var pointer = 0;
        var accumulator = 0;
        var visited = new HashSet<Integer>();

        while (true) {
            if (visited.contains(pointer)) return accumulator + "";
            visited.add(pointer);

            var instruction = instructions.get(pointer);

            switch (instruction.operation) {
                case ACC:
                    accumulator += instruction.value;
                    pointer++;
                    break;

                case JMP:
                    pointer += instruction.value;
                    break;

                case NOP:
                    pointer++;
                    break;
            }
        }
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var instructions = parseInput(input);

        for (var i = 0; i < instructions.size(); i++) {
            var instruction = instructions.get(i);
            if (instruction.operation == Operation.ACC) continue;

            var newInstructions = new ArrayList<>(instructions);
            var changedInstruction = new Instruction(
                instruction.operation == Operation.JMP ? Operation.NOP : Operation.JMP,
                instruction.value
            );
            newInstructions.set(i, changedInstruction);
            var result = execute(newInstructions);
            if (result != null) return result + "";
        }

        return "-not found-";
    }

    private List<Instruction> parseInput(String input) {
        var instructions = new ArrayList<Instruction>();

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" ");
            instructions.add(new Instruction(Operation.parse(parts[0]), Integer.parseInt(parts[1])));
        }

        return instructions;
    }

    private Integer execute(List<Instruction> instructions) {
        var pointer = 0;
        var accumulator = 0;
        var visited = new HashSet<Integer>();

        while (pointer < instructions.size()) {
            if (visited.contains(pointer)) return null;
            visited.add(pointer);

            var instruction = instructions.get(pointer);

            switch (instruction.operation) {
                case ACC:
                    accumulator += instruction.value;
                    pointer++;
                    break;

                case JMP:
                    pointer += instruction.value;
                    break;

                case NOP:
                    pointer++;
                    break;
            }
        }

        return accumulator;
    }

    private record Instruction(Operation operation, int value) {}

    private enum Operation {
        ACC, JMP, NOP;
        public static Operation parse(String input) {
            return switch (input) {
                case "acc" -> Operation.ACC;
                case "jmp" -> Operation.JMP;
                case "nop" -> Operation.NOP;
                default -> throw new IllegalArgumentException("Unknown operation: " + input);
            };
        }
    }
}

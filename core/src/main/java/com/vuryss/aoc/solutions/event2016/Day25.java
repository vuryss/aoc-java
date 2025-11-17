package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
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
        var instructions = new ArrayList<>(parseInstructions(input));
        long[] registers = new long[4];
        registers[0] = 0;

        while (!antennaWorks(Arrays.copyOf(registers, 4), instructions)) {
            registers[0]++;
        }

        return String.valueOf(registers[0]);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    private List<Instruction> parseInstructions(String input) {
        return Arrays.stream(input.trim().split("\n")).map(line -> {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());

            return new Instruction(operation, Arrays.copyOfRange(parts, 1, parts.length));
        }).toList();
    }

    private boolean antennaWorks(long[] registers, List<Instruction> instructions) {
        int index = 0;
        int outputSize = 0;
        long expectedOutput = 0;

        while (index < instructions.size()) {
            var instruction = instructions.get(index);

            switch (instruction.operation) {
                case CPY -> {
                    var value = instruction.operands[0].matches("^a|b|c|d$")
                        ? registers[instruction.operands[0].charAt(0) - 'a']
                        : Long.parseLong(instruction.operands[0]);

                    registers[instruction.operands[1].charAt(0) - 'a'] = value;
                }
                case INC -> registers[instruction.operands[0].charAt(0) - 'a']++;
                case DEC -> registers[instruction.operands[0].charAt(0) - 'a']--;
                case JNZ -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    if (value != 0) {
                        var steps = instruction.operands[1].matches("^a|b|c|d$")
                            ? registers[instruction.operands[1].charAt(0) - 'a']
                            : Integer.parseInt(instruction.operands[1]);
                        index += (int) steps;
                        continue;
                    }
                }
                case TGL -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];
                    value += index;

                    if (value < 0 || value >= instructions.size()) {
                        break;
                    }

                    var targetInstruction = instructions.get((int) value);

                    switch (targetInstruction.operation) {
                        case INC -> instructions.set((int) value, new Instruction(Operation.DEC, targetInstruction.operands));
                        case DEC, TGL -> instructions.set((int) value, new Instruction(Operation.INC, targetInstruction.operands));
                        case CPY -> instructions.set((int) value, new Instruction(Operation.JNZ, targetInstruction.operands));
                        case JNZ -> instructions.set((int) value, new Instruction(Operation.CPY, targetInstruction.operands));
                    }
                }
                case OUT -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    if (value != expectedOutput) {
                        return false;
                    }

                    outputSize++;
                    expectedOutput = 1 - expectedOutput;

                    if (outputSize == 10) {
                        return true;
                    }
                }
            }

            index++;
        }

        return false;
    }

    enum Operation { CPY, INC, DEC, JNZ, TGL, OUT }
    record Instruction(Operation operation, String[] operands) {
        @Override
        public @NotNull String toString() {
            return operation + " " + Arrays.toString(operands);
        }
    }
}

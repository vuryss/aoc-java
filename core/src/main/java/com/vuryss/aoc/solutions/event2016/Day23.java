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
public class Day23 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            cpy 2 a
            tgl a
            tgl a
            tgl a
            cpy 1 a
            dec a
            dec a
            """,
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var prototypeComputer = new PrototypeComputer(input, 7, 0, 0, 0);

        prototypeComputer.execute();

        return String.valueOf(prototypeComputer.getRegisterA());
        //var instructions = new ArrayList<>(parseInstructions(input));
        //long[] registers = new long[4];
        //registers[0] = 7;
        //
        //return String.valueOf(calculateRegisterA(registers, instructions));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var prototypeComputer = new PrototypeComputer(input, 12, 0, 0, 0);

        prototypeComputer.execute();

        return String.valueOf(prototypeComputer.getRegisterA());
        //var instructions = new ArrayList<>(parseInstructions(input));
        //long[] registers = new long[4];
        //registers[0] = 12;
        //
        //return String.valueOf(calculateRegisterA(registers, instructions));
    }

    private List<Instruction> parseInstructions(String input) {
        return Arrays.stream(input.trim().split("\n")).map(line -> {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());

            return new Instruction(operation, Arrays.copyOfRange(parts, 1, parts.length));
        }).toList();
    }

    private Long calculateRegisterA(long[] registers, List<Instruction> instructions) {
        int index = 0;

        while (index < instructions.size()) {
            var instruction = instructions.get(index);

            /**
             * cpy b c
             * inc a
             * dec c
             * jnz c -2
             * dec d
             * jnz d -5
             * Basically does D times (C=B times (a++ c--)) which means a = b * c; d = 0; c = 0; at the end
             */
            if (index == 4) {
                registers[0] += registers[3] * registers[1];
                registers[2] = 0;
                registers[3] = 0;
                index = 9;
                continue;
            }

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
            }

            index++;
        }

        return registers[0];
    }

    enum Operation { CPY, INC, DEC, JNZ, TGL }
    record Instruction(Operation operation, String[] operands) {
        @Override
        public @NotNull String toString() {
            return operation + " " + Arrays.toString(operands);
        }
    }
}

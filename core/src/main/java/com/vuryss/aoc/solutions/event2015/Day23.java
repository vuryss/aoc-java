package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
    enum Command {
        HLF, TPL, INC, JMP, JIE, JIO
    }

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
        var computer = new Computer();
        var instructions = parseInstructions(input);

        computer.run(instructions);

        return String.valueOf(computer.b);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new Computer();
        var instructions = parseInstructions(input);

        computer.a = 1;
        computer.run(instructions);

        return String.valueOf(computer.b);
    }

    private List<Instruction> parseInstructions(String input) {
        return Arrays.stream(input.trim().split("\n")).map(line -> {
            var parts = line.split(" ");
            var command = Command.valueOf(parts[0].toUpperCase());

            return switch (command) {
                case HLF, TPL, INC -> new Instruction(command, parts[1], 0);
                case JMP -> new Instruction(command, "", Integer.parseInt(parts[1]));
                case JIE, JIO -> new Instruction(command, parts[1].substring(0, 1), Integer.parseInt(parts[2]));
            };
        }).toList();
    }

    record Instruction(Command command, String register, int offset) {}

    static class Computer {
        public int a = 0;
        public int b = 0;
        public int instructionPointer = 0;

        public void run(List<Instruction> instructions) {
            while (instructionPointer < instructions.size()) {
                applyInstruction(instructions.get(instructionPointer));
            }
        }

        public void applyInstruction(Instruction instruction) {
            switch (instruction.command) {
                case HLF -> setRegisterValue(instruction.register, getRegisterValue(instruction.register) / 2);
                case TPL -> setRegisterValue(instruction.register, getRegisterValue(instruction.register) * 3);
                case INC -> setRegisterValue(instruction.register, getRegisterValue(instruction.register) + 1);
                case JMP -> {
                    instructionPointer += instruction.offset;
                    return;
                }
                case JIE -> {
                    if (getRegisterValue(instruction.register) % 2 == 0) {
                        instructionPointer += instruction.offset;
                        return;
                    }
                }
                case JIO -> {
                    if (getRegisterValue(instruction.register) == 1) {
                        instructionPointer += instruction.offset;
                        return;
                    }
                }
            }

            instructionPointer++;
        }

        public int getRegisterValue(String register) {
            return register.equals("a") ? a : b;
        }

        public void setRegisterValue(String register, int value) {
            if (register.equals("a")) {
                a = value;
            } else {
                b = value;
            }
        }
    }
}

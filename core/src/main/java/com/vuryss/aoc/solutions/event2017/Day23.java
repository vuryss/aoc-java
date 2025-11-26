package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
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
        var instructions = parseInstructions(input);
        var program = new Program(instructions);
        program.execute(true);
        return String.valueOf(program.mulCount);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var instructions = parseInstructions(input);
        var program = new Program(instructions);
        program.reg[0] = 1;
        program.execute(false);
        return String.valueOf(program.reg[7]);
    }

    private Instruction[] parseInstructions(String input) {
        var instructions = new ArrayList<Instruction>();

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());
            instructions.add(new Instruction(operation, Arrays.copyOfRange(parts, 1, parts.length)));
        }

        return instructions.toArray(new Instruction[0]);
    }

    private enum Operation {
        SET, SUB, MUL, JNZ
    }

    private record Instruction(Operation operation, String[] operands) {
        @Override
        public @NotNull String toString() {
            return operation + " " + Arrays.toString(operands);
        }
    }

    private static class Program {
        public long[] reg = new long[8];
        public int position = 0;
        public final Instruction[] instructions;
        public int mulCount = 0;

        public Program(Instruction[] instructions) {
            this.instructions = instructions;
        }

        public void execute(boolean part1) {
            while (position >= 0 && position < instructions.length) {
                var instr = instructions[position];

                if (!part1) {
                    /*
                     * With lots of reverse engineering it seems that this portion of the code tries to find if register
                     * b can be divided by any number between 2 and b - 1. If it can, it sets register f to 0, otherwise
                     * it sets it to 1. This basically drills down to checking if b is a prime number.
                     * Looks really obvious at the end, but until you see it - can be really hard to figure out.
                     */
                    if (position == 10) {
                        reg[5] = MathUtil.isPrime(reg[1]) ? 1 : 0; // f = isPrime(b)
                        reg[3] = reg[1]; // d = b
                        reg[4] = reg[1]; // e = b
                        reg[6] = 0; // g = 0
                        position = 24;
                        continue;
                    }
                }

                switch (instr.operation) {
                    case SET -> reg[instr.operands[0].charAt(0) - 'a'] = valueOf(reg, instr.operands[1]);
                    case SUB -> reg[instr.operands[0].charAt(0) - 'a'] -= valueOf(reg, instr.operands[1]);
                    case MUL -> {
                        reg[instr.operands[0].charAt(0) - 'a'] *= valueOf(reg, instr.operands[1]);
                        mulCount++;
                    }
                    case JNZ -> {
                        if (valueOf(reg, instr.operands[0]) != 0) {
                            position += (int) valueOf(reg, instr.operands[1]);
                            continue;
                        }
                    }
                }

                position++;
            }
        }

        private long valueOf(long[] registers, String value) {
            return isRegister(value) ? registers[value.charAt(0) - 'a'] : Integer.parseInt(value);
        }

        private boolean isRegister(String value) {
            char c = value.charAt(0);
            return c >= 'a' && c <= 'h';
        }
    }
}

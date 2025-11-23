package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            set a 1
            add a 2
            mul a a
            mod a 5
            snd a
            set a 0
            rcv a
            jgz a -1
            set a 1
            jgz a -2
            """,
            "4"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            snd 1
            snd 2
            snd p
            rcv a
            rcv b
            rcv c
            rcv d
            """,
            "3"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var instructions = parseInstructions(input);
        var outputQueue = new LinkedList<Long>();
        var program = new Program(0, instructions);
        program.outputQueue = outputQueue;

        program.execute(true);

        return String.valueOf(outputQueue.peekLast());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var instructions = parseInstructions(input);
        var program0 = new Program(0, instructions);
        var program1 = new Program(1, instructions);
        program0.outputQueue = program1.inputQueue;
        program1.outputQueue = program0.inputQueue;
        boolean result0, result1;

        do {
            result0 = program0.execute(false);
            result1 = program1.execute(false);
        } while (result0 || result1);

        return String.valueOf(program1.sentCount);
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

    private long valueOf(long[] registers, String value) {
        return isRegister(value) ? registers[value.charAt(0) - 'a'] : Integer.parseInt(value);
    }

    private boolean isRegister(String value) {
        char c = value.charAt(0);
        return c >= 'a' && c <= 'z';
    }

    private enum Operation {
        SND, SET, ADD, MUL, MOD, RCV, JGZ
    }

    private record Instruction(Operation operation, String[] operands) {}

    private static class Program {
        public long[] reg = new long[26];
        public int position = 0;
        public int id;
        public final Queue<Long> inputQueue = new LinkedList<>();
        public Queue<Long> outputQueue;
        public final Instruction[] instructions;
        public int sentCount = 0;

        public Program(int id, Instruction[] instructions) {
            this.id = id;
            this.reg['p' - 'a'] = id;
            this.instructions = instructions;
        }

        /**
         * If it returns true it means that it still executed something, false means it is waiting for input without
         * executing anything beforehand, or it has terminated.
         */
        public boolean execute(boolean part1) {
            var iterations = 0;

            while (position >= 0 && position < instructions.length) {
                var instr = instructions[position];

                switch (instr.operation) {
                    case SND -> {
                        outputQueue.add(valueOf(reg, instr.operands[0]));
                        sentCount++;
                    }
                    case SET -> reg[instr.operands[0].charAt(0) - 'a'] = valueOf(reg, instr.operands[1]);
                    case ADD -> reg[instr.operands[0].charAt(0) - 'a'] += valueOf(reg, instr.operands[1]);
                    case MUL -> reg[instr.operands[0].charAt(0) - 'a'] *= valueOf(reg, instr.operands[1]);
                    case MOD -> reg[instr.operands[0].charAt(0) - 'a'] %= valueOf(reg, instr.operands[1]);
                    case RCV -> {
                        if (part1) {
                            if (valueOf(reg, instr.operands[0]) != 0) {
                                return false;
                            }
                        } else {
                            if (inputQueue.isEmpty()) return iterations > 0;

                            reg[instr.operands[0].charAt(0) - 'a'] = inputQueue.poll();
                        }
                    }
                    case JGZ -> {
                        if (valueOf(reg, instr.operands[0]) > 0) {
                            position += (int) valueOf(reg, instr.operands[1]);
                            iterations++;
                            continue;
                        }
                    }
                }

                position++;
                iterations++;
            }

            return false;
        }

        private long valueOf(long[] registers, String value) {
            return isRegister(value) ? registers[value.charAt(0) - 'a'] : Integer.parseInt(value);
        }

        private boolean isRegister(String value) {
            char c = value.charAt(0);
            return c >= 'a' && c <= 'z';
        }
    }
}

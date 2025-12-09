package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
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
        var program = parseProgram(input);
        var registers = new long[6];
        int ip = 0;

        while (ip >= 0 && ip < program.instructions.length) {
            registers[program.ipRegister] = ip;

            if (ip == 18) {
                registers[1] = (registers[2] / 256);
                ip = 23;
                continue;
            }

            if (ip == 28) {
                return String.valueOf(registers[3]);
            }

            runOperation(registers, ip, program.instructions);
            ip = (int) registers[program.ipRegister] + 1;
        }

        return String.valueOf(registers[0]);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var program = parseProgram(input);
        var registers = new long[6];
        int ip = 0;

        var seen = new HashSet<Long>();
        long last = 0;

        while (ip >= 0 && ip < program.instructions.length) {
            registers[program.ipRegister] = ip;

            if (ip == 18) {
                registers[1] = (registers[2] / 256);
                ip = 23;
                continue;
            }

            if (ip == 28) {
                if (seen.contains(registers[3])) {
                    return String.valueOf(last);
                }

                seen.add(registers[3]);
                last = registers[3];
            }

            runOperation(registers, ip, program.instructions);
            ip = (int) registers[program.ipRegister] + 1;
        }

        return "-not found-";
    }

    private Program parseProgram(String input) {
        var instructions = new ArrayList<Instruction>();
        var lines = input.trim().split("\n");
        var ipRegister = Integer.parseInt(lines[0].split(" ")[1]);

        for (var line: Arrays.copyOfRange(lines, 1, lines.length)) {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());
            int[] operands = Arrays.stream(parts, 1, parts.length).mapToInt(Integer::parseInt).toArray();
            instructions.add(new Instruction(operation, operands));
        }

        return new Program(ipRegister, instructions.toArray(new Instruction[0]));
    }

    private void runOperation(long[] registers, int ip, Instruction[] instructions) {
        int A = instructions[ip].operands[0];
        int B = instructions[ip].operands[1];
        int C = instructions[ip].operands[2];

        switch (instructions[ip].operation) {
            case ADDR -> registers[C] = registers[A] + registers[B];
            case ADDI -> registers[C] = registers[A] + B;
            case MULR -> registers[C] = registers[A] * registers[B];
            case MULI -> registers[C] = registers[A] * B;
            case BANR -> registers[C] = registers[A] & registers[B];
            case BANI -> registers[C] = registers[A] & B;
            case BORR -> registers[C] = registers[A] | registers[B];
            case BORI -> registers[C] = registers[A] | B;
            case SETR -> registers[C] = registers[A];
            case SETI -> registers[C] = A;
            case GTIR -> registers[C] = A > registers[B] ? 1 : 0;
            case GTRI -> registers[C] = registers[A] > B ? 1 : 0;
            case GTRR -> registers[C] = registers[A] > registers[B] ? 1 : 0;
            case EQIR -> registers[C] = A == registers[B] ? 1 : 0;
            case EQRI -> registers[C] = registers[A] == B ? 1 : 0;
            case EQRR -> registers[C] = registers[A] == registers[B] ? 1 : 0;
        }
    }

    private enum Operation {
        ADDR, ADDI, MULR, MULI, BANR, BANI, BORR, BORI, SETR, SETI, GTIR, GTRI, GTRR, EQIR, EQRI, EQRR
    }

    record Instruction(Operation operation, int[] operands) {}
    record Program (int ipRegister, Instruction[] instructions) {}
}

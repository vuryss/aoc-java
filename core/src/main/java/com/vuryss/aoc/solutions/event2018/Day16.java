package com.vuryss.aoc.solutions.event2018;

import com.google.common.primitives.Ints;
import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Before: [3, 2, 1, 1]
            9 2 1 2
            After:  [3, 2, 2, 1]
            
            
            1 2 3 4
            """,
            "1"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var inputData = parseInput(input);
        int count = 0;
        Operation[] allOperations = Operation.values();

        for (var sample: inputData.samples) {
            var parts = sample.split("\n");
            var registers = Ints.toArray(StringUtil.ints(parts[0]));
            var instruction = StringUtil.ints(parts[1]);
            var resultRegisters = Ints.toArray(StringUtil.ints(parts[2]));
            int matches = 0;

            for (var operation: allOperations) {
                var registersCopy = Arrays.copyOf(registers, registers.length);
                executeOperation(registersCopy, operation, instruction.get(1), instruction.get(2), instruction.get(3));

                if (Arrays.equals(registersCopy, resultRegisters)) {
                    matches++;
                }
            }

            if (matches >= 3) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var inputData = parseInput(input);
        int count = 0;
        Operation[] allOperations = Operation.values();
        var operationMap = new HashMap<Integer, Operation>(allOperations.length);

        while (operationMap.size() < allOperations.length) {
            for (var sample : inputData.samples) {
                var parts = sample.split("\n");
                var registers = Ints.toArray(StringUtil.ints(parts[0]));
                var instruction = StringUtil.ints(parts[1]);
                var resultRegisters = Ints.toArray(StringUtil.ints(parts[2]));
                Set<Operation> matchedOperations = new HashSet<>();

                for (var operation : allOperations) {
                    var registersCopy = Arrays.copyOf(registers, registers.length);
                    executeOperation(registersCopy, operation, instruction.get(1), instruction.get(2), instruction.get(3));

                    if (Arrays.equals(registersCopy, resultRegisters) && !operationMap.containsValue(operation)) {
                        matchedOperations.add(operation);
                    }
                }

                if (matchedOperations.size() == 1) {
                    operationMap.put(instruction.getFirst(), matchedOperations.iterator().next());
                }
            }
        }

        var registers = new int[] {0, 0, 0, 0};

        for (var op: inputData.program) {
            executeOperation(registers, operationMap.get(op[0]), op[1], op[2], op[3]);
        }

        return String.valueOf(registers[0]);
    }

    private InputData parseInput(String input) {
        var inputParts = input.trim().split("\n\n\n\n");
        var samplesInput = inputParts[0].trim().split("\n\n");
        var programInput = inputParts[1].trim().split("\n");
        var samples = new String[samplesInput.length];
        var program = new int[programInput.length][4];

        System.arraycopy(samplesInput, 0, samples, 0, samplesInput.length);

        for (var i = 0; i < programInput.length; i++) {
            program[i] = Ints.toArray(StringUtil.ints(programInput[i]));
        }

        return new InputData(samples, program);
    }

    private void executeOperation(int[] registers, Operation operation, int A, int B, int C) {
        switch (operation) {
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

    private record InputData(String[] samples, int[][] program) {}
}

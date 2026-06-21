package com.vuryss.aoc.solutions.event2019;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IntcodeComputer implements Runnable {
    private static final Map<Integer, Opcode> opcodes = Map.of(
        1, Opcode.ADD,
        2, Opcode.MULTIPLY,
        3, Opcode.INPUT,
        4, Opcode.OUTPUT,
        5, Opcode.JUMP_IF_TRUE,
        6, Opcode.JUMP_IF_FALSE,
        7, Opcode.LESS_THAN,
        8, Opcode.EQUALS,
        99,  Opcode.HALT
    );
    int position = 0;
    int[] memory;
    int[] originalMemory;
    Queue<Integer> inputQueue = new ConcurrentLinkedQueue<>();
    Queue<Integer> outputQueue = new ConcurrentLinkedQueue<>();
    boolean diagnostics = false;

    public IntcodeComputer(String programCode) {
        this(programCode.split(","));
    }

    public IntcodeComputer(String[] parts) {
        memory = new int[parts.length];

        for (var i = 0; i < parts.length; i++) {
            memory[i] = Integer.parseInt(parts[i]);
        }

        originalMemory = memory.clone();
    }

    public void reset() {
        position = 0;
        memory = originalMemory.clone();
        inputQueue = new ConcurrentLinkedQueue<>();
        outputQueue = new ConcurrentLinkedQueue<>();
    }

    public void run() {
        while (true) {
            var instruction = memory[position];
            var opcode = opcodes.get(instruction % 100);
            instruction /= 100;
            var p1mode = ParameterMode.POSITION;
            var p2mode = ParameterMode.POSITION;
            var p3mode = ParameterMode.POSITION;
            if (instruction % 10 == 1) p1mode = ParameterMode.IMMEDIATE;
            instruction /= 10;
            if (instruction % 10 == 1) p2mode = ParameterMode.IMMEDIATE;
            instruction /= 10;
            if (instruction % 10 == 1) p3mode = ParameterMode.IMMEDIATE;
            int p1, p2, p3;

            switch (opcode) {
                case Opcode.ADD:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    p3 = memory[position + 3];
                    memory[p3] = p1 + p2;
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Add " + p1 + "+" + p2 + "=" + (p1 + p2) + " storing at: " + p3);
                    break;

                case Opcode.MULTIPLY:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    p3 = memory[position + 3];
                    memory[p3] = p1 * p2;
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Multiply " + p1 + "*" + p2 + "=" + (p1 * p2) + " storing at: " + p3);
                    break;

                case Opcode.INPUT:
                    while (inputQueue.isEmpty()) Thread.yield();
                    int input = inputQueue.poll();
                    p1 = memory[position + 1];
                    memory[p1] = input;
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Input: " + input + " storing at: " + p1);
                    break;

                case Opcode.OUTPUT:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    outputQueue.add(p1);
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Output: " + p1);
                    break;

                case Opcode.JUMP_IF_TRUE:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    position = p1 != 0 ? p2 : position + opcode.values;
                    break;

                case Opcode.JUMP_IF_FALSE:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    position = p1 == 0 ? p2 : position + opcode.values;
                    break;

                case Opcode.LESS_THAN:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    memory[memory[position + 3]] = p1 < p2 ? 1 : 0;
                    position += opcode.values;
                    break;

                case Opcode.EQUALS:
                    p1 = p1mode == ParameterMode.POSITION ? memory[memory[position + 1]] : memory[position + 1];
                    p2 = p2mode == ParameterMode.POSITION ? memory[memory[position + 2]] : memory[position + 2];
                    p3 = memory[position + 3];
                    if (diagnostics)
                        System.out.println("If " + p1 + "==" + p2 + " store 1 at " + p3 + " otherwise 0");
                    memory[p3] = p1 == p2 ? 1 : 0;
                    position += opcode.values;
                    break;

                case Opcode.HALT:
                    return;
            }
        }
    }

    private enum ParameterMode { POSITION, IMMEDIATE }
    private enum Opcode {
        ADD(4), MULTIPLY(4),
        INPUT(2), OUTPUT(2),
        JUMP_IF_TRUE(3), JUMP_IF_FALSE(3),
        LESS_THAN(4), EQUALS(4),
        HALT(1)
        ;

        public final int values;

        Opcode(int values) {
            this.values = values;
        }
    }
}
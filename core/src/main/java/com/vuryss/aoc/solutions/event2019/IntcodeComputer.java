package com.vuryss.aoc.solutions.event2019;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IntcodeComputer implements Runnable {
    private static final Map<Long, Opcode> opcodes = Map.of(
        1L, Opcode.ADD,
        2L, Opcode.MULTIPLY,
        3L, Opcode.INPUT,
        4L, Opcode.OUTPUT,
        5L, Opcode.JUMP_IF_TRUE,
        6L, Opcode.JUMP_IF_FALSE,
        7L, Opcode.LESS_THAN,
        8L, Opcode.EQUALS,
        9L, Opcode.RELATIVE_BASE,
        99L,  Opcode.HALT
    );
    private Thread thread;
    private long position = 0;
    private long relativeBase = 0;
    private Map<Long, Long> memory;
    private final Map<Long, Long> originalMemory;
    private BlockingQueue<Long> inputQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Long> outputQueue = new LinkedBlockingQueue<>();
    boolean diagnostics = false;

    public IntcodeComputer(String programCode) {
        this(programCode.split(","));
    }

    public IntcodeComputer(String[] parts) {
        memory = new HashMap<>();

        for (var i = 0; i < parts.length; i++) {
            mem(i, Long.parseLong(parts[i]));
        }

        originalMemory = new HashMap<>(memory);
    }

    public void start() {
        if (thread != null) throw new IllegalStateException("Already started");
        thread = Thread.ofVirtual().start(this);
    }

    public void reset() {
        waitTillShutdown();
        thread = null;
        position = 0;
        memory = new HashMap<>(originalMemory);
        inputQueue = new LinkedBlockingQueue<>();
        outputQueue = new LinkedBlockingQueue<>();
    }

    public void input(long value) {
        inputQueue.add(value);
    }

    public long input() {
        try { return inputQueue.take(); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public long takeSingleOutput() {
        try { return outputQueue.take(); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public List<Long> consumeOutputUntilShutdown() {
        var result = new ArrayList<Long>();

        while (isRunning()) {
            if (hasOutput()) {
                result.add(takeSingleOutput());
            }
        }

        return result;
    }

    public boolean isRunning() {
        return thread != null && thread.isAlive();
    }

    public boolean hasOutput() {
        return !outputQueue.isEmpty();
    }

    public void waitTillShutdown() {
        if (thread == null) return;
        try { thread.join(); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public void run() {
        while (true) {
            var instruction = mem(position);
            var opcode = opcodes.get(instruction % 100);
            instruction /= 100;
            int[] pos = new int[opcode.values - 1];

            for (int i = 0; i < opcode.values - 1; i++) {
                var mode = switch ((int) (instruction % 10)) {
                    case 0 -> ParameterMode.POSITION;
                    case 1 -> ParameterMode.IMMEDIATE;
                    case 2 -> ParameterMode.RELATIVE;
                    default -> throw new RuntimeException("Invalid parameter mode!");
                };
                instruction /= 10;
                pos[i] = (int) switch (mode) {
                    case ParameterMode.POSITION -> mem(position + i + 1);
                    case ParameterMode.IMMEDIATE -> position + i + 1;
                    case ParameterMode.RELATIVE -> relativeBase + mem(position + i + 1);
                };
            }

            switch (opcode) {
                case Opcode.ADD:
                    mem(pos[2], mem(pos[0]) + mem(pos[1]));
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Add " + mem(pos[0]) + "+" + mem(pos[1]) + "=" + (mem(pos[0]) + mem(pos[1])) + " storing at: " + pos[2]);
                    break;

                case Opcode.MULTIPLY:
                    mem(pos[2], mem(pos[0]) * mem(pos[1]));
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Multiply " + mem(pos[0]) + "*" + mem(pos[1]) + "=" + (mem(pos[0]) * mem(pos[1])) + " storing at: " + pos[2]);
                    break;

                case Opcode.INPUT:
                    long input = input();
                    mem(pos[0], input);
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Input: " + input + " storing at: " + pos[0]);
                    break;

                case Opcode.OUTPUT:
                    outputQueue.add(mem(pos[0]));
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("Output: " + mem(pos[0]));
                    break;

                case Opcode.JUMP_IF_TRUE:
                    position = (mem(pos[0]) != 0 ? mem(pos[1]) : position + opcode.values);
                    break;

                case Opcode.JUMP_IF_FALSE:
                    position = (mem(pos[0]) == 0 ? mem(pos[1]) : position + opcode.values);
                    break;

                case Opcode.LESS_THAN:
                    mem(pos[2], mem(pos[0]) < mem(pos[1]) ? 1 : 0);
                    position += opcode.values;
                    break;

                case Opcode.EQUALS:
                    mem(pos[2], mem(pos[0]) == mem(pos[1]) ? 1 : 0);
                    position += opcode.values;
                    if (diagnostics)
                        System.out.println("If " + pos[0] + "==" + pos[1] + " store 1 at " + pos[2] + " otherwise 0");
                    break;

                case Opcode.RELATIVE_BASE:
                    relativeBase += (int) mem(pos[0]);
                    position += opcode.values;
                    break;

                case Opcode.HALT:
                    return;
            }
        }
    }

    public long mem(long position) {
        return memory.getOrDefault(position, 0L);
    }

    public void mem(long position, long value) {
        memory.put(position, value);
    }

    private enum ParameterMode { POSITION, IMMEDIATE, RELATIVE }
    private enum Opcode {
        ADD(4), MULTIPLY(4),
        INPUT(2), OUTPUT(2),
        JUMP_IF_TRUE(3), JUMP_IF_FALSE(3),
        LESS_THAN(4), EQUALS(4),
        RELATIVE_BASE(2),
        HALT(1)
        ;

        public final int values;

        Opcode(int values) {
            this.values = values;
        }
    }
}
package com.vuryss.aoc.solutions.event2019;

public class IntcodeComputer {
    int position = 0;
    int[] memory;
    int[] originalMemory;

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
    }

    public void run() {
        while (true) {
            switch (memory[position]) {
                case 1:
                    memory[memory[position + 3]] = memory[memory[position + 1]] + memory[memory[position + 2]];
                    position += 4;
                    break;

                case 2:
                    memory[memory[position + 3]] = memory[memory[position + 1]] * memory[memory[position + 2]];
                    position += 4;
                    break;

                case 99:
                    return;

                default:
                    throw new RuntimeException("Opcode not supported: " + memory[position]);
            }
        }
    }
}

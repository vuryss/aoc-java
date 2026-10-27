package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.util.Algorithm;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

public class PrototypeComputer {
    private final ArrayList<Instruction> instructions = new ArrayList<>();
    private final long[] registers = new long[4];
    private int instructionPointer = 0;
    private final int instructionsCount;
    private Function<Long, Void> outputCallback = null;

    public PrototypeComputer(String program) {
        this(program, 0, 0, 0, 0);
    }

    public PrototypeComputer(String program, int registerA, int registerB, int registerC, int registerD) {
        parseProgram(program);
        registers[0] = registerA;
        registers[1] = registerB;
        registers[2] = registerC;
        registers[3] = registerD;
        instructionsCount = instructions.size();
    }

    public long getRegisterA() {
        return registers[0];
    }

    public void setOutputCallback(Function<Long, Void> outputCallback) {
        this.outputCallback = outputCallback;
    }

    public void reboot(int registerA, int registerB, int registerC, int registerD) {
        registers[0] = registerA;
        registers[1] = registerB;
        registers[2] = registerC;
        registers[3] = registerD;
        instructionPointer = 0;
    }

    public void halt() {
        instructionPointer = instructionsCount;
    }

    public void execute() {
        while (instructionPointer < instructionsCount) {
            var instruction = instructions.get(instructionPointer);

            switch (instruction.operation) {
                case CPY -> {
                    var value = instruction.operands[0].matches("^a|b|c|d$")
                        ? registers[instruction.operands[0].charAt(0) - 'a']
                        : Long.parseLong(instruction.operands[0]);

                    registers[instruction.operands[1].charAt(0) - 'a'] = value;
                }
                case INC -> registers[instruction.operands[0].charAt(0) - 'a']++;
                case INC_BY -> {
                    var value = instruction.operands[0].matches("^a|b|c|d$")
                        ? registers[instruction.operands[0].charAt(0) - 'a']
                        : Long.parseLong(instruction.operands[0]);

                    registers[instruction.operands[1].charAt(0) - 'a'] += value;
                }
                case DEC -> registers[instruction.operands[0].charAt(0) - 'a']--;
                case MUL_BY -> {
                    var value = instruction.operands[0].matches("^a|b|c|d$")
                        ? registers[instruction.operands[0].charAt(0) - 'a']
                        : Long.parseLong(instruction.operands[0]);

                    registers[instruction.operands[1].charAt(0) - 'a'] *= value;
                }
                case JNZ -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    if (value != 0) {
                        var steps = instruction.operands[1].matches("^a|b|c|d$")
                            ? registers[instruction.operands[1].charAt(0) - 'a']
                            : Integer.parseInt(instruction.operands[1]);
                        instructionPointer += (int) steps;
                        continue;
                    }
                }
                case TGL -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];
                    value += instructionPointer;

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
                case FIB -> {
                    var fibRegister = instruction.operands[0];
                    var loopRegister = instruction.operands[1];
                    registers[fibRegister.charAt(0) - 'a'] = Algorithm.fib(registers[loopRegister.charAt(0) - 'a'] + 2);
                }
                case OUT -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    if (outputCallback != null) {
                        outputCallback.apply(value);
                    }
                    //
                    //if (value != expectedOutput) {
                    //    return false;
                    //}
                    //
                    //outputSize++;
                    //expectedOutput = 1 - expectedOutput;
                    //
                    //if (outputSize == 10) {
                    //    return true;
                    //}
                }
            }

            instructionPointer++;
        }
    }

    private void parseProgram(String program) {
        for (var line: program.trim().split("\n")) {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());

            instructions.add(new Instruction(operation, Arrays.copyOfRange(parts, 1, parts.length)));
        }

        instructionOptimizer();
    }

    private void instructionOptimizer() {
        var loops = new ArrayList<Loop>();

        // Loop detection
        for (var i = 0; i < instructions.size() - 1; i++) {
            var instruction = instructions.get(i);
            var nextInstruction = instructions.get(i + 1);

            if (
                instruction.operation == Operation.DEC
                && nextInstruction.operation == Operation.JNZ
                && instruction.operands[0].equals(nextInstruction.operands[0])
                && nextInstruction.operands[1].matches("^-\\d")
            ) {
                var loopInstructionCount = Math.abs(Integer.parseInt(nextInstruction.operands[1]));
                char loopRegister = nextInstruction.operands[0].charAt(0);
                loops.add(new Loop(i - loopInstructionCount + 1, loopInstructionCount, loopRegister + ""));
            }
        }

        loops.sort(Comparator.comparingInt(l -> l.size));

        for (var loop: loops) {
            if (loop.size == 2) {
                // Optimize small loops with pattern [INC a; DEC b; JNZ b -N] -> [INC_BY b a; CPY 0 b; JNZ b -N]
                if (instructions.get(loop.start).operation != Operation.INC) {
                    throw new RuntimeException("Instruction optimization failed, unexpected loop 2 first operation: " + instructions.get(loop.start));
                }
                var incRegister = instructions.get(loop.start).operands[0];
                var decRegister = instructions.get(loop.start + 1).operands[0];

                instructions.set(
                    loop.start,
                    new Instruction(Operation.INC_BY, new String[] { decRegister, incRegister })
                );
                instructions.set(
                    loop.start + 1,
                    new Instruction(Operation.CPY, new String[] { "0", decRegister })
                );
            } else if (loop.size == 5) {
                // Optimize multiply loops with pattern [CPY x a; INC_BY a b; ... repeat until N]
                // They contain the smaller optimization from above - [INC a; DEC b; JNZ b -N] -> [INC_BY b a; CPY 0 b; JNZ b -N]
                // Which has to be verified too
                if (
                    instructions.get(loop.start).operation != Operation.CPY
                    || instructions.get(loop.start + 1).operation != Operation.INC_BY
                    || !instructions.get(loop.start).operands[1].equals(instructions.get(loop.start + 1).operands[0])
                ) {
                    throw new RuntimeException("Instruction optimization failed, unexpected 5 step loop");
                }
                var incRegister = instructions.get(loop.start + 1).operands[1];
                var mulRegister = instructions.get(loop.start).operands[1];
                instructions.set(
                    loop.start + 1,
                    new Instruction(Operation.MUL_BY, new String[] { loop.loopRegister, mulRegister })
                );
                instructions.set(
                    loop.start + 2,
                    new Instruction(Operation.INC_BY, new String[] { mulRegister, incRegister })
                );
                instructions.set(
                    loop.start + 3,
                    new Instruction(Operation.CPY, new String[] { "0", mulRegister })
                );
                instructions.set(
                    loop.start + 4,
                    new Instruction(Operation.CPY, new String[] { "0", loop.loopRegister})
                );
            } else if (loop.size == 6) {
                /*
                  Detect and optimize special case: fibonacci sequence
                  Raw form:
                  	CPY [a, c]
                  	INC [a]
                  	DEC [b]
                  	JNZ [b, -2]
                  	CPY [c, b]
                  	DEC [d]
                  	JNZ [d, -6]
                  With smaller loop optimization it loops like:
                    CPY [a, c]
                  	INC_BY [b, a]
                  	CPY [0, b]
                  	JNZ [b, -2]
                  	CPY [c, b]
                  	DEC [d]
                  	JNZ [d, -6]
                  We should be able to detect this pattern for Day 12 and use a special instruction to execute it
                 */
                var i_0 = instructions.get(loop.start);
                var i_1 = instructions.get(loop.start + 1);
                var i_4 = instructions.get(loop.start + 4);

                if (
                    i_0.operation != Operation.CPY
                    || i_1.operation != Operation.INC_BY
                    || i_4.operation != Operation.CPY
                ) {
                    throw new RuntimeException("Instruction optimization failed, unexpected 6 step loop");
                }

                var fibRegister = i_0.operands[0];
                instructions.set(
                    loop.start + 1,
                    new Instruction(Operation.FIB, new String[] { fibRegister, loop.loopRegister })
                );
                instructions.set(
                    loop.start + 5,
                    new Instruction(Operation.CPY, new String[] { "0", loop.loopRegister})
                );
            } else {
                //System.out.println("Unoptimized Loop found at instruction " + loop.start + " until zero: " + loop.loopRegister);
                //
                //for (var j = loop.start; j < loop.start + loop.size; j++) {
                //    System.out.print("\t");
                //    System.out.println(instructions.get(j));
                //}
            }
        }
    }

    enum Operation { CPY, INC, DEC, JNZ, TGL, OUT, INC_BY, MUL_BY, FIB }

    record Instruction(Operation operation, String[] operands) {
        @Override
        public @NotNull String toString() {
            return operation + " " + Arrays.toString(operands);
        }
    }

    private record Loop(int start, int size, String loopRegister) {}
}

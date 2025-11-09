package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Algorithm;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Optimization 1:
 *      assembunny:
 *          cpy a c
 *          inc a
 *          dec b
 *          jnz b -2
 *          cpy c b
 *          dec d
 *          jnz d -6
 *      equals to code:
 *          do {
 * 	            c = a
 * 	            do {
 * 		            a++
 * 		            b--
 * 	            } while (b != 0)
 * 	            b = c
 * 	            d--
 *          } while(d != 0)
 *      which can be optimized to:
 *          do {
 *              c = a
 * 	            a += b
 * 	            b = c
 * 	            d--
 *          } while (d != 0)
 *      which is basically a fibonacci sequence
 * ---
 * Optimization 2:
 *      assembunny:
 *          cpy 13 c
 *          cpy 14 d
 *          inc a
 *          dec d
 *          jnz d -2
 *          dec c
 *          jnz c -5
 *      equals to code:
 *          c = 13
 *          do {
 *              d = 14
 *              do {
 *                  a++
 *                  d--
 *              } while (d != 0)
 *              c--
 *          } while (c != 0)
 *      which is basically a multiplication
 *          a += 13 * 14
 */
@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            cpy 41 a
            inc a
            inc a
            dec a
            jnz a 2
            dec a
            """,
            "42"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var instructions = parseInstructions(input);
        long[] registers = new long[4];

        return String.valueOf(calculateRegisterA(registers, instructions));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var instructions = parseInstructions(input);
        long[] registers = new long[4];
        registers[2] = 1;

        return String.valueOf(calculateRegisterA(registers, instructions));
    }

    private List<Instruction> parseInstructions(String input) {
        return Arrays.stream(input.trim().split("\n")).map(line -> {
            var parts = line.split(" ");
            var operation = Operation.valueOf(parts[0].toUpperCase());

            return new Instruction(operation, Arrays.copyOfRange(parts, 1, parts.length));
        }).toList();
    }

    private long calculateRegisterA(long[] registers, List<Instruction> instructions) {
        int index = 0;

        while (index < instructions.size()) {
            var instruction = instructions.get(index);

            // First loop optimization
            if (index == 9) {
                registers[0] = Algorithm.fib(registers[3] + 2);
                index = 16;
                continue;
            }

            // Second loop optimization
            if (index == 18) {
                registers[0] += registers[2] * registers[3];
                break;
            }

            switch (instruction.operation) {
                case CPY -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    registers[instruction.operands[1].charAt(0) - 'a'] = value;
                }
                case INC -> registers[instruction.operands[0].charAt(0) - 'a']++;
                case DEC -> registers[instruction.operands[0].charAt(0) - 'a']--;
                case JNZ -> {
                    var value = instruction.operands[0].chars().allMatch(Character::isDigit)
                        ? Long.parseLong(instruction.operands[0])
                        : registers[instruction.operands[0].charAt(0) - 'a'];

                    if (value != 0) {
                        index += Integer.parseInt(instruction.operands[1]);
                        continue;
                    }
                }
            }

            index++;
        }

        return registers[0];
    }

    enum Operation { CPY, INC, DEC, JNZ }
    record Instruction(Operation operation, String[] operands) {}
}

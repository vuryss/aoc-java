package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

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
        var computer = new IntcodeComputer(input.trim());
        var result = 0L;

        computer.start();

        String instructions =
            "NOT C J\n" + // Jump if the third tile ahead is a hole
            "OR D T\n"  + // Store whether the fourth tile ahead is ground
            "AND T J\n" + // Jump if the third tile is a hole and the fourth tile is ground
            "NOT A T\n" + // Store whether the first tile ahead is a hole
            "OR T J\n"  + // Jump if the first tile is a hole, or if the third tile is a hole and the fourth tile is ground
            "WALK\n";

        for (var ch: instructions.toCharArray()) computer.input(ch);
        while (computer.isRunning()) while (computer.hasOutput()) result = computer.takeSingleOutput();

        return result + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        var result = 0L;

        computer.start();

        String instructions =
            "NOT A T\n" + // Store whether the first tile ahead is a hole
            "NOT B J\n" + // Jump if the second tile ahead is a hole
            "OR T J\n"  + // Jump if either the first or the second tile ahead is a hole
            "NOT C T\n" + // Store whether the third tile ahead is a hole
            "OR T J\n"  + // Jump if any of the first three tiles ahead is a hole
            "AND D J\n" + // Jump only if the fourth tile ahead, where the droid will land, is ground
            "NOT E T\n" + // Store whether the fifth tile ahead is a hole
            "NOT T T\n" + // Invert the temporary register so it indicates whether the fifth tile is ground
            "OR H T\n"  + // Continue only if the fifth or the eighth tile ahead is ground
            "AND T J\n" + // Jump if there is a hole within the first three tiles, the landing tile is ground, and the droid can continue afterwards
            "RUN\n";

        for (var ch: instructions.toCharArray()) computer.input(ch);
        while (computer.isRunning()) while (computer.hasOutput()) result = computer.takeSingleOutput();

        return result + "";
    }
}

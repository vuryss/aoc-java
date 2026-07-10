package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
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
        var count = 0L;

        for (var x = 0; x < 50; x++) {
            for (var y = 0; y < 50; y++) {
                count += isInBeam(computer, x, y) ? 1 : 0;
            }
        }

        return count + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        var x = 0;
        int size = 100 - 1;

        // Below row 100 we cannot fit 100x100 square
        for (var y = size; true; y++) {
            while (!isInBeam(computer, x, y)) x++;

            if (isInBeam(computer, x + size, y - size)) {
                return (x * 10000 + y - size) + "";
            }
        }
    }

    private boolean isInBeam(IntcodeComputer computer, int x, int y) {
        computer.reset();
        computer.start();
        computer.input(x);
        computer.input(y);

        return computer.takeSingleOutput() == 1;
    }
}

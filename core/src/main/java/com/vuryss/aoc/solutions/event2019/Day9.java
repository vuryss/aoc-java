package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "1102,34915192,34915192,7,4,7,99,0", "1219070632396864",
            "104,1125899906842624,99", "1125899906842624"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.input(1L);
        computer.start();
        return computer.takeSingleOutput() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.input(2L);
        computer.start();
        return computer.takeSingleOutput() + "";
    }
}

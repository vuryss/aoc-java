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
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.inputQueue.add(1L);
        computer.run();
        return computer.outputQueue.poll() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.inputQueue.add(2L);
        computer.run();
        return computer.outputQueue.poll() + "";
    }
}

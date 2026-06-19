package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "3,9,8,9,10,9,4,9,99,-1,8", "1",
            "3,9,7,9,10,9,4,9,99,-1,8", "0",
            "3,3,1108,-1,8,3,4,3,99", "1",
            "3,3,1107,-1,8,3,4,3,99", "0",
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", "1000"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.inputQueue.add(1);
        computer.run();

        return computer.outputQueue.stream().map(String::valueOf).collect(Collectors.joining()).replaceAll("^0*", "");
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.inputQueue.add(isTest ? 8 : 5);
        computer.run();

        return computer.outputQueue.stream().map(String::valueOf).collect(Collectors.joining());
    }
}

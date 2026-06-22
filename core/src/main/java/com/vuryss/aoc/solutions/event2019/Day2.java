package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "1,9,10,3,2,3,11,0,99,30,40,50", "3500",
            "1,0,0,0,99", "2",
            "2,3,0,3,99", "2",
            "2,4,4,5,99,0", "2",
            "1,1,1,4,99,5,6,0,99", "30"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());

        if (!isTest) {
            computer.mem(1,  12);
            computer.mem(2, 2);
        }

        computer.run();

        return computer.mem(0) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());

        for (var noun = 0; noun < 100; noun++) {
            for (var verb = 0; verb < 100; verb++) {
                computer.reset();
                computer.mem(1, noun);
                computer.mem(2, verb);
                computer.run();

                if (computer.mem(0) == 19690720) {
                    return (100 * noun + verb) + "";
                }
            }
        }

        return "-not-found-";
    }
}

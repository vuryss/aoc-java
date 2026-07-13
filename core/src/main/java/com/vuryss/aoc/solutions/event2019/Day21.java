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

//        String instructions = """
//            NOT C J // J = true if 3rd hole
//            OR D T // T = true if 4th ground
//            AND T J // J = true if (3rd hole && 4th ground)
//            NOT A T // T = true if 1st hole
//            OR T J // J = true if (3rd hole && 4th ground) or 1st hole
//            WALK
//            """;

        String instructions = """
            NOT C J
            OR D T
            AND T J
            NOT A T
            OR T J
            WALK
            """;

        for (var ch: instructions.toCharArray()) computer.input(ch);
        while (computer.isRunning()) while (computer.hasOutput()) result = computer.takeSingleOutput();

        return result + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        var result = 0L;

        computer.start();

//        String instructions = """
//            NOT A T // T = true if 1st = hole
//            NOT B J // J = true if 2nd = hole
//            OR T J // J = true if (1st hole || 2nd hole)
//            NOT C T // T = true if 3rd = hole
//            OR T J // J = true if (1st hole || 2nd hole || 3rd hole)
//            AND D J // J = true if (1st hole || 2nd hole || 3rd hole) && 4th ground
//            NOT E T // T = true if 5th hole
//            NOT T T // T = true if 5th ground
//            OR H T // T = true if (5th ground || 8th ground)
//            AND T J // J = true if (1st hole || 2nd hole || 3rd hole) && 4th ground && (5th ground || 8th ground)
//            RUN
//            """;

        String instructions = """
            NOT A T
            NOT B J
            OR T J
            NOT C T
            OR T J
            AND D J
            NOT E T
            NOT T T
            OR H T
            AND T J
            RUN
            """;

        for (var ch: instructions.toCharArray()) computer.input(ch);
        while (computer.isRunning()) while (computer.hasOutput()) result = computer.takeSingleOutput();

        return result + "";
    }
}

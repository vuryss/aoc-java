package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
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
        var counter = 0;
        int i = 0;
        computer.start();

        for (var output: computer.consumeOutputUntilShutdown()) {
            if (++i % 3 == 0 && output == 2) counter++;
        }

        return counter + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        int i = 0;
        long x = 0, y = 0, id = 0, ballX = 0, paddleX = 0, score = 0;
        computer.mem(0, 2);
        computer.start();

        while (computer.isRunning() || computer.hasOutput()) {
            while (computer.hasOutput()) {
                var output = computer.takeSingleOutput();
                switch (++i % 3) {
                    case 0 -> id = output;
                    case 1 -> x = output;
                    case 2 -> y = output;
                }

                if (i % 3 == 0) {
                    if (x == -1 && y == 0) {
                        score = id;
                    } else if (id == 4) {
                        ballX = x;
                    } else if (id == 3) {
                        paddleX = x;
                    }
                }
            }

            if (computer.isWaitingForInput() && !computer.hasOutput()) {
                computer.input(Long.compare(ballX, paddleX));
            }
        }

        return score + "";
    }
}

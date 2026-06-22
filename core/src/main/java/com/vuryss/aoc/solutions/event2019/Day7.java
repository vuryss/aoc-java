package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Combinatorics;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0", "43210",
            "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0", "54321",
            "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0", "65210"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5", "139629729",
            "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10", "18216"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        var permutations = Combinatorics.permutations(List.of(0, 1, 2, 3, 4), 5);
        var max = Long.MIN_VALUE;

        for (var permutation : permutations) {
            var value = 0L;

            for (var i = 0; i < 5; i++) {
                computer.inputQueue.add(permutation.get(i).longValue());
                computer.inputQueue.add(value);
                computer.run();
                value = computer.outputQueue.poll();
                computer.reset();
            }

            max = Math.max(max, value);
        }

        return max + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var amplifiers = new IntcodeComputer[5];
        var threads = new Thread[5];
        var permutations = Combinatorics.permutations(List.of(5, 6, 7, 8, 9), 5);
        var max = Long.MIN_VALUE;

        for (var i = 0; i < 5; i++) amplifiers[i] = new IntcodeComputer(input.trim());

        for (var permutation : permutations) {
            for (var i = 0; i < 5; i++) {
                amplifiers[i].reset();
                amplifiers[i].inputQueue.add(permutation.get(i).longValue());
                threads[i] = new Thread(amplifiers[i]);
                threads[i].start();
            }
            var value = 0L;

            outer:
            while (true) {
                for (var i = 0; i < 5; i++) {
                    amplifiers[i].inputQueue.add(value);
                    while (amplifiers[i].outputQueue.isEmpty()) {
                        if (!threads[i].isAlive()) break outer;
                        Thread.yield();
                    }
                    value = amplifiers[i].outputQueue.poll();
                }
            }

            for (var i = 0; i < 5; i++) {
                try { threads[i].join(); } catch (InterruptedException ignored) { }
            }

            max = Math.max(max, value);
        }

        return max + "";
    }
}

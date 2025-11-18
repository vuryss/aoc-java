package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    private final OutputState outputState = new OutputState();

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
        var registerA = 0;
        var prototypeComputer = new PrototypeComputer(input, registerA, 0, 0, 0);

        while (!antennaWorks(prototypeComputer)) {
            registerA++;
            prototypeComputer.reboot(registerA, 0, 0, 0);
        }

        return String.valueOf(registerA);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    private boolean antennaWorks(PrototypeComputer computer) {
        outputState.size = 0;
        outputState.expectedOutput = 0;
        outputState.success = false;

        Function<Long, Void> outputFn = (value) -> {
            if (value != outputState.expectedOutput) {
                computer.halt();
                return null;
            }

            outputState.size++;
            outputState.expectedOutput = 1 - outputState.expectedOutput;

            if (outputState.size == 10) {
                outputState.success = true;
                computer.halt();
            }

            return null;
        };

        computer.setOutputCallback(outputFn);
        computer.execute();

        return outputState.success;
    }

    private static class OutputState {
        public int size = 0;
        public long expectedOutput = 0;
        public boolean success = false;
    }
}

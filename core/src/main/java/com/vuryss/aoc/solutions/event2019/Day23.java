package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
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
        var computers = new IntcodeComputer[50];

        for (int i = 0; i < computers.length; i++){
            computers[i] = new IntcodeComputer(input.trim());
            computers[i].start();
            computers[i].input(i);
        }

        while (true) {
            for (IntcodeComputer computer : computers) {
                if (computer.hasOutput()) {
                    long[] outputs = new long[3];
                    outputs[0] = computer.takeSingleOutput();
                    outputs[1] = computer.takeSingleOutput();
                    outputs[2] = computer.takeSingleOutput();

                    if (outputs[0] == 255) return outputs[2] + "";

                    computers[(int) outputs[0]].input(outputs[1]);
                    computers[(int) outputs[0]].input(outputs[2]);
                }

                if (computer.isWaitingForInput()) computer.input(-1);
            }
        }
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computers = new IntcodeComputer[50];

        for (int i = 0; i < computers.length; i++){
            computers[i] = new IntcodeComputer(input.trim());
            computers[i].start();
            computers[i].input(i);
        }

        long lastNatY = 0;
        long natX = 0;
        long natY = 0;
        boolean natReceived = false;

        while (true) {
            int idle = 0;
            boolean packetSent = false;

            for (IntcodeComputer computer : computers) {
                if (computer.hasOutput()) {
                    long[] outputs = new long[3];
                    outputs[0] = computer.takeSingleOutput();
                    outputs[1] = computer.takeSingleOutput();
                    outputs[2] = computer.takeSingleOutput();

                    if (outputs[0] == 255) {
                        natX = outputs[1];
                        natY = outputs[2];
                        natReceived = true;
                    } else {
                        computers[(int) outputs[0]].input(outputs[1]);
                        computers[(int) outputs[0]].input(outputs[2]);
                        packetSent = true;
                    }
                }

                if (computer.isWaitingForInput()) {
                    computer.input(-1);
                    idle++;
                }
            }

            if (idle == 50 && natReceived && !packetSent) {
                computers[0].input(natX);
                computers[0].input(natY);

                if (lastNatY == natY) return natY + "";
                lastNatY = natY;
            }
        }
    }
}

package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.LetterOCR;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
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
        computer.start();

        var position = new Point(0, 0);
        var direction = Direction.U;
        var grid = new HashMap<Point, Boolean>();

        while (computer.isRunning()) {
            computer.input(grid.getOrDefault(position, false) == false ? 0 : 1);
            while (computer.isRunning() && !computer.hasOutput()) Thread.yield();
            if (!computer.isRunning()) break;

            var color = computer.takeSingleOutput();
            var turn = computer.takeSingleOutput();

            grid.put(position, color != 0);
            if (turn == 0) direction = direction.turnLeft();
            else direction = direction.turnRight();

            position = position.goInDirection(direction);
        }

        return grid.size() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var computer = new IntcodeComputer(input.trim());
        computer.start();

        var position = new Point(0, 0);
        var direction = Direction.U;
        var grid = new HashMap<Point, Boolean>();
        grid.put(position, true);

        while (computer.isRunning()) {
            computer.input(grid.getOrDefault(position, false) == false ? 0 : 1);
            while (computer.isRunning() && !computer.hasOutput()) Thread.yield();
            if (!computer.isRunning()) break;

            var color = computer.takeSingleOutput();
            var turn = computer.takeSingleOutput();

            grid.put(position, color != 0);
            if (turn == 0) direction = direction.turnLeft();
            else direction = direction.turnRight();

            position = position.goInDirection(direction);
        }

        var ocrPoints = new HashSet<Point>();

        for (var entrySet: grid.entrySet()) {
            if (entrySet.getValue()) ocrPoints.add(entrySet.getKey());
        }

        return LetterOCR.decode46(ocrPoints);
    }
}

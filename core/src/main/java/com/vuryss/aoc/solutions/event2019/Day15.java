package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.PointLong;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    private HashMap<PointLong, Integer> map;
    private PointLong oxygenPosition;
    private IntcodeComputer computer;

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
        exploreWholeMap(input);

        var queue = new LinkedList<State>();
        var visited = new HashSet<PointLong>();
        visited.add(new PointLong(0, 0));
        queue.addFirst(new State(new PointLong(0, 0), 0));

        while (!queue.isEmpty()) {
            var state = queue.removeFirst();

            for (var next: state.position.adjacentTopLeftOrder()) {
                if (visited.contains(next)) continue;

                switch (map.get(next)) {
                    case 0 -> {}
                    case 1 -> {
                        visited.add(next);
                        queue.addLast(new State(next, state.steps + 1));
                    }
                    case 2 -> {
                        return (state.steps + 1) + "";
                    }
                }
            }
        }

        return "-halt-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        exploreWholeMap(input);

        var queue = new LinkedList<State>();
        var visited = new HashSet<PointLong>();
        visited.add(oxygenPosition);
        queue.addFirst(new State(oxygenPosition, 0));
        var maxSteps = 0;

        while (!queue.isEmpty()) {
            var state = queue.removeFirst();
            maxSteps = state.steps;

            for (var next: state.position.adjacentTopLeftOrder()) {
                if (visited.contains(next)) continue;

                switch (map.get(next)) {
                    case 0 -> {}
                    case 1 -> {
                        visited.add(next);
                        queue.addLast(new State(next, state.steps + 1));
                    }
                    case 2 -> throw new RuntimeException("Should not be here");
                }
            }
        }

        return maxSteps + "";
    }

    private void exploreWholeMap(String input) {
        var position = new PointLong(0, 0);

        oxygenPosition = null;

        map = new HashMap<>();
        map.put(position, 1);

        computer = new IntcodeComputer(input.trim());
        computer.start();

        explore(position);

        computer.shutdown();
    }

    private void explore(PointLong position) {
        int i = 0;
        int[] direction = new int[]{1, 3, 4, 2};
        int[] oppositeDirection = new int[]{2, 4, 3, 1};

        for (var point: position.adjacentTopLeftOrder()) {
            i++;
            if (map.containsKey(point)) continue;

            computer.input(direction[i-1]);
            var result = computer.takeSingleOutput();
            map.put(point, (int) result);

            if (result > 0) {
                if (result == 2) oxygenPosition = point;
                explore(point);
                computer.input(oppositeDirection[i-1]);
                if (computer.takeSingleOutput() == 0) throw new RuntimeException("Error - robot went in wrong direction");
            }
        }
    }

    private record State(PointLong position, int steps) {}
}

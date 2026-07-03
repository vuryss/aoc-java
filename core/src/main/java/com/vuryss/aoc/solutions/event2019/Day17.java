package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.PointLong;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    private final Set<Long> robotAscii = Set.of((long) '^',(long)  'v',(long)  '>',(long)  '<');
    private final long scaffold = '#';
    private Direction robotDirection;
    private PointLong robotPosition;

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
        var grid = generateGrid(input);
        long sum = 0;

        nextPoint:
        for (var entry : grid.entrySet()) {
            if (!entry.getValue().equals(scaffold)) continue;

            for (var adj: entry.getKey().adjacentTopLeftOrder()) {
                if (!grid.containsKey(adj) || !grid.get(adj).equals(scaffold)) continue nextPoint;
            }

            sum += entry.getKey().x * entry.getKey().y;
        }

        return sum + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = generateGrid(input);
        var steps = 0;
        var path = new StringBuilder();
        Objects.requireNonNull(robotDirection, "direction is null");
        Objects.requireNonNull(robotPosition, "position is null");

        while (true) {
            // Forward?
            var forward = robotPosition.goInDirection(robotDirection);

            if (grid.getOrDefault(forward, 0L) == scaffold) {
                robotPosition = forward;
                steps++;
                continue;
            }

            // Left?
            var left = robotPosition.goInDirection(robotDirection.turnLeft());

            if (grid.getOrDefault(left, 0L) == scaffold) {
                if (steps > 0) {
                    path.append(steps).append(",");
                }
                robotPosition = left;
                steps = 1;
                path.append("L,");
                robotDirection = robotDirection.turnLeft();
                continue;
            }

            // Right?
            var right = robotPosition.goInDirection(robotDirection.turnRight());

            if (grid.getOrDefault(right, 0L) == scaffold) {
                if (steps > 0) {
                    path.append(steps).append(",");
                }
                robotPosition = right;
                steps = 1;
                path.append("R,");
                robotDirection = robotDirection.turnRight();
                continue;
            }

            path.append(steps);
            break;
        }

        var result = findCombination(path.toString(), List.of('A', 'B', 'C'), "") + "\nn\n";
        var computer = new IntcodeComputer(input.trim());
        computer.mem(0, 2);
        result.chars().forEach(computer::input);
        computer.start();

        return computer.consumeOutputUntilShutdown().getLast() + "";
    }

    private HashMap<PointLong, Long> generateGrid(String input) {
        var computer = new IntcodeComputer(input.trim());
        computer.start();
        computer.waitTillShutdown();
        var grid = new HashMap<PointLong, Long>();
        long x = 0, y = 0;

        while (computer.hasOutput()) {
            var output = computer.takeSingleOutput();

            if (output == 10) {
                y++;
                x = 0;
                continue;
            } else if (robotAscii.contains(output)) {
                robotDirection = Direction.fromArrowChar((char) output);
                robotPosition = new PointLong(x, y);
                output = scaffold;
            }

            grid.put(new PointLong(x++, y), output);
        }

        return grid;
    }

    private String findCombination(String path, List<Character> movementFunctions, String input) {
        if (movementFunctions.isEmpty()) {
            return path.length() <= 20 && Arrays.stream(path.split(",")).allMatch(this::isFunction)
                ? path + '\n' + input
                : null;
        }

        var pathIndex = 0;
        var pathArray = path.split(",");
        var section = new StringBuilder();
        while (pathIndex < pathArray.length && isFunction(pathArray[pathIndex])) pathIndex++;

        outer:
        while (true) {
            var needed = 2;

            while (needed > 0) {
                if (pathIndex >= pathArray.length || isFunction(pathArray[pathIndex++])) break outer;
                if (!section.isEmpty()) section.append(",");
                section.append(pathArray[pathIndex-1]);
                needed--;
            }

            if (section.length() > 20) break;

            var newPath = path.replaceAll("(?<=,|^)" + section + "(?=,|$)", movementFunctions.getFirst().toString());
            var newInput = input.isEmpty() ? section.toString() : input + '\n' + section;
            var success = findCombination(newPath, movementFunctions.subList(1, movementFunctions.size()), newInput);

            if (null != success) return success;
        }

        return null;
    }

    private boolean isFunction(String path) {
        return path.equals("A") || path.equals("B") || path.equals("C");
    }
}

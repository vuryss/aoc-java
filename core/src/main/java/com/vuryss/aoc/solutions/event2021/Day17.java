package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import java.util.*;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            target area: x=20..30, y=-10..-5
            """,
            "45"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            target area: x=20..30, y=-10..-5
            """,
            "112"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var target = StringUtil.sints(input);
        var targetMinY = Math.min(target.get(2), target.get(3));
        var maxVelocityY = Math.abs(targetMinY) - 1;

        return String.valueOf(maxVelocityY * (maxVelocityY + 1) / 2);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var target = StringUtil.sints(input);
        var xVelocitySteps = new HashMap<Integer, Set<Integer>>();
        var yVelocitySteps = new HashMap<Integer, Set<Integer>>();
        var targetMinY = Math.min(target.get(2), target.get(3));
        var targetMaxY = Math.max(target.get(2), target.get(3));
        var maxVelocityX = target.get(1) + 1;
        var maxVelocityY = Math.abs(targetMinY) - 1;
        var exactVelocityX = new HashMap<Integer, Integer>(); // Contains all x velocities that are always on target after a certain number of steps

        // Make a list of all x velocities that match target after x steps (also store ones that match the target exactly when they stop moving forward)
        for (var xVelocity = 1; xVelocity < maxVelocityX; xVelocity++) {
            var distance = 0;
            var velocity = xVelocity;
            var steps = 0;

            while (velocity > 0) {
                distance += velocity;
                steps++;

                if (distance >= target.getFirst() && distance <= target.get(1)) {
                    var listOfSteps = xVelocitySteps.getOrDefault(xVelocity, new HashSet<>());
                    listOfSteps.add(steps);
                    xVelocitySteps.put(xVelocity, listOfSteps);

                    if (velocity == 1) {
                        exactVelocityX.put(xVelocity, steps);
                    }
                }

                velocity--;
            }
        }

        // Make a list of all y velocities that match target after y steps
        for (var yVelocity = targetMinY; yVelocity <= maxVelocityY; yVelocity++) {
            var distance = 0;
            var velocity = yVelocity;
            var steps = 0;

            while (distance >= targetMinY) {
                distance += velocity;
                steps++;

                if (distance >= targetMinY && distance <= targetMaxY) {
                    var listOfSteps = yVelocitySteps.getOrDefault(yVelocity, new HashSet<>());
                    listOfSteps.add(steps);
                    yVelocitySteps.put(yVelocity, listOfSteps);
                }

                velocity--;
            }
        }

        var count = 0;

        for (var yVelocity: yVelocitySteps.keySet()) {
            var ySteps = yVelocitySteps.get(yVelocity);

            for (var xVelocity: xVelocitySteps.keySet()) {
                var xSteps = xVelocitySteps.get(xVelocity);

                if (ySteps.stream().anyMatch(xSteps::contains)) {
                    count++;
                    continue;
                }

                if (exactVelocityX.containsKey(xVelocity)) {
                    if (ySteps.stream().anyMatch(steps -> steps > exactVelocityX.get(xVelocity))) {
                        count++;
                    }
                }
            }
        }

        return String.valueOf(count);
    }
}
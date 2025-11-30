package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Step C must be finished before step A can begin.
            Step C must be finished before step F can begin.
            Step A must be finished before step B can begin.
            Step A must be finished before step D can begin.
            Step B must be finished before step E can begin.
            Step D must be finished before step E can begin.
            Step F must be finished before step E can begin.
            """,
            "CABDFE"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Step C must be finished before step A can begin.
            Step C must be finished before step F can begin.
            Step A must be finished before step B can begin.
            Step A must be finished before step D can begin.
            Step B must be finished before step E can begin.
            Step D must be finished before step E can begin.
            Step F must be finished before step E can begin.
            """,
            "15"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var stepsDependencies = new HashMap<Character, Set<Character>>();
        var steps = new TreeSet<Character>();

        for (var line: lines) {
            var parts = Regex.matchGroups("Step (\\w) must be finished before step (\\w) can begin.", line);
            assert parts != null;
            stepsDependencies.computeIfAbsent(parts.get(1).charAt(0), k -> new HashSet<>()).add(parts.get(0).charAt(0));
            steps.add(parts.get(0).charAt(0));
            steps.add(parts.get(1).charAt(0));
        }

        var result = new StringBuilder();

        while (!steps.isEmpty()) {
            // Check if there are unsatisfied dependencies for any of the steps
            for (var step: steps) {
                if (stepsDependencies.containsKey(step) && !stepsDependencies.get(step).isEmpty()) {
                    continue;
                }

                result.append(step);
                steps.remove(step);
                stepsDependencies.values().forEach(s -> s.remove(step));
                break;
            }
        }

        return result.toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var stepsDependencies = new HashMap<Character, Set<Character>>();
        var steps = new TreeSet<Character>();
        var stepTemp = steps;

        for (var line: lines) {
            var parts = Regex.matchGroups("Step (\\w) must be finished before step (\\w) can begin.", line);
            assert parts != null;
            stepsDependencies.computeIfAbsent(parts.get(1).charAt(0), k -> new HashSet<>()).add(parts.get(0).charAt(0));
            steps.add(parts.get(0).charAt(0));
            steps.add(parts.get(1).charAt(0));
        }

        var result = new StringBuilder();
        var length = steps.size();
        var seconds = -1;
        var freeWorkers = isTest ? 2 : 5;
        var addSeconds = isTest ? 0 : 60;
        int[] workerWorkLeft = new int[freeWorkers];
        char[] workerCurrentStep = new char[freeWorkers];
        Arrays.fill(workerWorkLeft, -1);

        nextSecond:
        while (result.length() < length) {
            seconds++;
            steps = stepTemp;

            // Finish those in progress
            for (var i = 0; i < workerWorkLeft.length; i++) {
                if (workerWorkLeft[i] > 0) {
                    workerWorkLeft[i]--;
                }

                if (workerWorkLeft[i] == 0) {
                    char finishedStep = workerCurrentStep[i];
                    stepsDependencies.values().forEach(s -> s.remove(finishedStep));
                    steps.remove(workerCurrentStep[i]);
                    workerWorkLeft[i] = -1;
                    result.append(workerCurrentStep[i]);
                }
            }

            // Start new ones
            stepTemp = new TreeSet<>(steps);

            for (var step: steps) {
                if (stepsDependencies.containsKey(step) && !stepsDependencies.get(step).isEmpty()) {
                    continue;
                }

                var workerIndex = ArrayUtils.indexOf(workerWorkLeft, -1);

                if (workerIndex == ArrayUtils.INDEX_NOT_FOUND) {
                    continue nextSecond;
                }

                stepTemp.remove(step);
                workerWorkLeft[workerIndex] = addSeconds + step - 'A' + 1;
                workerCurrentStep[workerIndex] = step;
            }
        }

        return String.valueOf(seconds);
    }

    private Map<Character, Set<Character>> parseInput(String input) {
        var lines = input.trim().split("\n");
        var map = new HashMap<Character, Set<Character>>();

        for (var line: lines) {
            var parts = Regex.matchGroups("Step (\\w) must be finished before step (\\w) can begin.", line);
            assert parts != null;
            map.computeIfAbsent(parts.get(1).charAt(0), k -> new HashSet<>()).add(parts.get(0).charAt(0));
        }

        return map;
    }
}

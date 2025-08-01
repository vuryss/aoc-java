package com.vuryss.aoc;

import com.vuryss.aoc.solutions.DayInterface;

public class Game {
    public Game(int year, int day, boolean isTest) {
        if (isTest) {
            executeWithTestInput(year, day);
            return;
        }

        executeWithUserInput(year, day);
    }

    private void executeWithUserInput(int year, int day) {
        var solution = getEventDaySolution(year, day);
        var input = new InputResolver().getForEventDay(year, day);

        System.out.println("\n\n");

        long startTime, executionInMs;

        startTime = System.nanoTime();
        System.out.printf("\tSolution for day %s part 1: %s\n", day, solution.part1Solution(input));
        executionInMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("\tExecution time day %s part 1: %s ms\n", day, executionInMs);

        System.out.println("\n");

        startTime = System.nanoTime();
        System.out.printf("\tSolution for day %s part 2: %s\n", day, solution.part2Solution(input));
        executionInMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("\tExecution time day %s part 2: %s ms\n", day, executionInMs);

        System.out.println("\n");
    }

    private void executeWithTestInput(int year, int day) {
        var solution = getEventDaySolution(year, day);

        System.out.println();
        System.out.println("Executing tests for part 1...");

        var testNumber = 0;

        for (var testSet: solution.part1Tests().entrySet()) {
            System.out.printf("\tExecuting test %s ... ", ++testNumber);

            var testResult = solution.part1Solution(testSet.getKey());

            if (testResult.equals(testSet.getValue())) {
                System.out.println("success!");
            } else {
                System.out.printf("error! Expected: %s | Result: %s\n", testSet.getValue(), testResult);
            }
        }

        System.out.println("Executing tests for part 2...");

        testNumber = 0;

        for (var testSet: solution.part2Tests().entrySet()) {
            System.out.printf("\tExecuting test %s ... ", ++testNumber);

            var testResult = solution.part2Solution(testSet.getKey());

            if (testResult.equals(testSet.getValue())) {
                System.out.println("success!");
            } else {
                System.out.printf("error! Expected: %s | Result: %s\n", testSet.getValue(), testResult);
            }
        }
    }

    private DayInterface getEventDaySolution(int year, int day) {
        var className = String.format("com.vuryss.aoc.solutions.event%s.Day%s", year, day);

        try {
            Class<?> dynamicClass = Class.forName(className);
            return (DayInterface) dynamicClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Solution for event %s day %s not found!", year, day), e);
        }
    }
}

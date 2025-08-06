package com.vuryss.aoc;

import com.vuryss.aoc.cache.CachedAdventOfCodeClient;
import com.vuryss.aoc.solutions.DayInterface;
import jakarta.enterprise.context.ApplicationScoped;
import picocli.CommandLine;

@ApplicationScoped
public class SingleSolutionExecutor {
    private final CachedAdventOfCodeClient cachedAdventOfCodeClient;

    public SingleSolutionExecutor(CachedAdventOfCodeClient cachedAdventOfCodeClient) {
        this.cachedAdventOfCodeClient = cachedAdventOfCodeClient;
    }

    public void execute(int year, int day, boolean test, boolean validate, boolean overwriteCache) {
        var solution = SolutionRepository.getSolutionFor(year, day);

        if (solution.isEmpty()) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                "@|bold,red No solution available for year %d day %d|@", year, day
            )));
            return;
        }

        if (test) {
            executeTestScenarios(year, day, solution.get());
            return;
        }

        var input = cachedAdventOfCodeClient.getPlayerInput(year, day, overwriteCache);

        if (validate) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                "@|bold,blue Executing and validating solution for year %s day %s|@\n", year, day
            )));

            var answers = cachedAdventOfCodeClient.getCorrectAnswers(year, day, overwriteCache);
            var part1ExecutionStart = System.nanoTime();
            var part1Result = solution.get().part1Solution(input);
            var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;
            var part1Success = part1Result.equals(answers.part1());

            System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                "\tPart 1 result: @|bold,cyan %s|@ @|bold,%s %s|@ (executed in %.2f ms)\n",
                part1Result,
                part1Success ? "green" : "red",
                part1Success ? "CORRECT" : "INCORRECT",
                part1ExecutionTime / 1_000_000.0
            )));

            var part2ExecutionStart = System.nanoTime();
            var part2Result = solution.get().part2Solution(input);
            var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;
            var part2Success = part2Result.equals(answers.part2());

            System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                "\tPart 2 result: @|bold,cyan %s|@ @|bold,%s %s|@ (executed in %.2f ms)\n",
                part2Result,
                part2Success ? "green" : "red",
                part2Success ? "CORRECT" : "INCORRECT",
                part2ExecutionTime / 1_000_000.0
            )));

            return;
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "@|bold,blue Executing solution for year %s day %s|@\n", year, day
        )));

        var part1ExecutionStart = System.nanoTime();
        var part1Result = solution.get().part1Solution(input);
        var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;

        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 1 result: @|bold,green %s|@ (executed in %.2f ms)\n",
            part1Result,
            part1ExecutionTime / 1_000_000.0
        )));

        var part2ExecutionStart = System.nanoTime();
        var part2Result = solution.get().part2Solution(input);
        var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;

        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 2 result: @|bold,green %s|@ (executed in %.2f ms)",
            part2Result,
            part2ExecutionTime / 1_000_000.0
        )));
        System.out.println();
    }

    private void executeTestScenarios(int year, int day, DayInterface solution) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "@|bold,blue Executing test scenarios for year %s day %s|@\n",
            year, day
        )));

        var index = 1;

        for (var testSet : solution.part1Tests().entrySet()) {
            var result = solution.part1Solution(testSet.getKey());

            if (result.equals(testSet.getValue())) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                    "\tPart 1 Test %d: @|bold,green PASSED|@", index++
                )));
            } else {
                System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                    "\tPart 1 Test %d: @|bold,red FAILED|@ (result: %s, expected: %s)",
                    index++, result, testSet.getValue()
                )));
            }
        }

        System.out.println();
        index = 1;

        for (var testSet : solution.part2Tests().entrySet()) {
            var result = solution.part2Solution(testSet.getKey());

            if (result.equals(testSet.getValue())) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                    "\tPart 2 Test %d: @|bold,green PASSED|@", index++
                )));
            } else {
                System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                    "\tPart 2 Test %d: @|bold,red FAILED|@ (result: %s, expected: %s)",
                    index++, result, testSet.getValue()
                )));
            }
        }

        System.out.println();
    }
}

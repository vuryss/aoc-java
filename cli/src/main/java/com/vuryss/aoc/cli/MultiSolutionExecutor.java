package com.vuryss.aoc.cli;

import com.vuryss.aoc.SolutionRepository;
import com.vuryss.aoc.cli.cache.CachedAdventOfCodeClient;
import jakarta.enterprise.context.ApplicationScoped;
import picocli.CommandLine;

import java.util.List;

@ApplicationScoped
public class MultiSolutionExecutor {
    private final CachedAdventOfCodeClient cachedAdventOfCodeClient;

    public MultiSolutionExecutor(CachedAdventOfCodeClient cachedAdventOfCodeClient) {
        this.cachedAdventOfCodeClient = cachedAdventOfCodeClient;
    }

    public void execute(List<Integer> years, List<Integer> days, boolean test, boolean validate, boolean overwriteCache) {
        for (var year: years) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                "@|bold,blue Executing solution for year %s|@\n", year
            )));

            for (var day: days) {
                var solution = SolutionRepository.getSolutionFor(year, day);

                if (solution.isEmpty()) {
                    System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                        "\t@|bold,red No solution available for year %d day %d|@", year, day
                    )));
                    continue;
                }

                if (test) {
                    var part1Success = true;
                    var part2Success = true;

                    for (var testSet : solution.get().part1Tests().entrySet()) {
                        part1Success &= solution.get().part1Solution(testSet.getKey(), true).equals(testSet.getValue());
                    }

                    for (var testSet : solution.get().part2Tests().entrySet()) {
                        part2Success &= solution.get().part2Solution(testSet.getKey(), true).equals(testSet.getValue());
                    }

                    System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                        "\tEvent %d Day %d | Part1: @|bold,%s %s|@ | Part2: @|bold,%s %s|@",
                        year,
                        day,
                        part1Success ? "green" : "red",
                        part1Success ? "PASSED" : "FAILED",
                        part2Success ? "green" : "red",
                        part2Success ? "PASSED" : "FAILED"
                    )));
                    continue;
                }

                var input = cachedAdventOfCodeClient.getPlayerInput(year, day, overwriteCache);

                if (validate) {
                    var answers = cachedAdventOfCodeClient.getCorrectAnswers(year, day, overwriteCache);
                    var part1ExecutionStart = System.nanoTime();
                    var part1Result = solution.get().part1Solution(input, false);
                    var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;
                    var part1Success = part1Result.equals(answers.part1());
                    var part2ExecutionStart = System.nanoTime();
                    var part2Result = solution.get().part2Solution(input, false);
                    var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;
                    var part2Success = part2Result.equals(answers.part2());

                    System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                        "\tEvent %d Day %d | Part1: @|bold,cyan %s|@ @|bold,%s %s|@ (Executed in %.2f ms) | Part2: @|bold,cyan %s|@ @|bold,%s %s|@ (Executed in %.2f ms)",
                        year,
                        day,
                        part1Result,
                        part1Success ? "green" : "red",
                        part1Success ? "CORRECT" : "INCORRECT",
                        part1ExecutionTime / 1_000_000.0,
                        part2Result,
                        part2Success ? "green" : "red",
                        part2Success ? "CORRECT" : "INCORRECT",
                        part2ExecutionTime / 1_000_000.0
                    )));
                    continue;
                }

                var part1ExecutionStart = System.nanoTime();
                var part1Result = solution.get().part1Solution(input, false);
                var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;
                var part2ExecutionStart = System.nanoTime();
                var part2Result = solution.get().part2Solution(input, false);
                var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;

                System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
                    "\tEvent %d Day %d | Part1: @|bold,cyan %s|@ (Executed in %.2f ms) | Part2: @|bold,cyan %s|@ (Executed in %.2f ms)",
                    year,
                    day,
                    part1Result,
                    part1ExecutionTime / 1_000_000.0,
                    part2Result,
                    part2ExecutionTime / 1_000_000.0
                )));
            }
        }
    }
}

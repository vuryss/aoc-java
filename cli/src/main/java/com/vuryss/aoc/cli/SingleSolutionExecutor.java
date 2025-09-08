package com.vuryss.aoc.cli;

import com.vuryss.aoc.SolutionRepository;
import com.vuryss.aoc.cli.cache.CachedAdventOfCodeClient;
import com.vuryss.aoc.cli.output.OutputInterface;
import com.vuryss.aoc.solutions.SolutionInterface;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SingleSolutionExecutor {
    private final CachedAdventOfCodeClient cachedAdventOfCodeClient;
    private OutputInterface output;

    public SingleSolutionExecutor(CachedAdventOfCodeClient cachedAdventOfCodeClient) {
        this.cachedAdventOfCodeClient = cachedAdventOfCodeClient;
    }

    public void setOutput(OutputInterface output) {
        this.output = output;
    }

    public void execute(
        int year,
        int day,
        boolean test,
        boolean validate,
        boolean overwriteCache,
        String manualInput
    ) {
        var solution = SolutionRepository.getSolutionFor(year, day);

        if (solution.isEmpty()) {
            throw new CliException(String.format("No solution available for year %d day %d", year, day));
        }

        if (test) {
            executeTestScenarios(year, day, solution.get());
            return;
        }

        String input = manualInput == null
            ? cachedAdventOfCodeClient.getPlayerInput(year, day, overwriteCache)
            : manualInput;

        if (validate) {
            this.output.note(String.format("Executing and validating solution for year %s day %s", year, day));

            var answers = cachedAdventOfCodeClient.getCorrectAnswers(year, day, overwriteCache);

            var part1ExecutionStart = System.nanoTime();
            var part1Result = solution.get().part1Solution(input);
            var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;
            var part1Success = part1Result.equals(answers.part1());

            this.output.part1(part1Result, part1ExecutionTime / 1_000_000.0, part1Success);

            var part2ExecutionStart = System.nanoTime();
            var part2Result = solution.get().part2Solution(input);
            var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;
            var part2Success = part2Result.equals(answers.part2());

            this.output.part2(part2Result, part2ExecutionTime / 1_000_000.0, part2Success);

            return;
        }

        this.output.note(String.format("Executing solution for year %s day %s", year, day));

        var part1ExecutionStart = System.nanoTime();
        var part1Result = solution.get().part1Solution(input);
        var part1ExecutionTime = System.nanoTime() - part1ExecutionStart;

        this.output.part1(part1Result, part1ExecutionTime / 1_000_000.0);

        var part2ExecutionStart = System.nanoTime();
        var part2Result = solution.get().part2Solution(input);
        var part2ExecutionTime = System.nanoTime() - part2ExecutionStart;

        this.output.part2(part2Result, part2ExecutionTime / 1_000_000.0);
    }

    private void executeTestScenarios(int year, int day, SolutionInterface solution) {
        this.output.note(String.format("Executing test scenarios for year %s day %s", year, day));

        for (var testSet : solution.part1Tests().entrySet()) {
            var solutionResult = solution.part1Solution(testSet.getKey());
            var success = solutionResult.equals(testSet.getValue());
            this.output.testPart1(solutionResult, testSet.getValue(), success);
        }

        for (var testSet : solution.part2Tests().entrySet()) {
            var solutionResult = solution.part2Solution(testSet.getKey());
            var success = solutionResult.equals(testSet.getValue());
            this.output.testPart2(solutionResult, testSet.getValue(), success);
        }
    }
}

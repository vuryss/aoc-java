package com.vuryss.aoc.cli;

import com.vuryss.aoc.cli.output.JsonOutput;
import com.vuryss.aoc.cli.output.PrettyPrintOutput;
import com.vuryss.aoc.config.AoCConfig;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandLine.Command(name = "solve", description = "Execute AoC solutions")
public class SolveCommand implements Runnable {
    @CommandLine.Option(names = {"-y", "--year"}, description = "Year of the challenge")
    int year;

    @CommandLine.Option(names = {"-d", "--day"}, description = "Day of the challenge")
    int day;

    @CommandLine.Option(names = {"-t", "--test"}, description = "Use test data")
    boolean test = false;

    @CommandLine.Option(names = {"-v", "--validate"}, description = "Validate answers of already completed puzzles")
    boolean validate = false;

    @CommandLine.Option(names = {"-c", "--overwrite-cache"}, description = "Overwrite the cache")
    boolean overwriteCache = false;

    @CommandLine.Option(names = {"-i", "--input-from-stdin"}, description = "Read input from stdin")
    boolean inputFromStdin = false;

    @CommandLine.Option(names = {"-j", "--json-output"}, description = "Output as JSON")
    boolean jsonOutput = false;

    private final SingleSolutionExecutor singleSolutionExecutor;
    private final MultiSolutionExecutor multiSolutionExecutor;

    public SolveCommand(
        SingleSolutionExecutor singleSolutionExecutor,
        MultiSolutionExecutor multiSolutionExecutor
    ) {
        this.singleSolutionExecutor = singleSolutionExecutor;
        this.multiSolutionExecutor = multiSolutionExecutor;
    }

    @Override
    public void run() {
        var years = resolveYears();
        var days = resolveDays();
        var singleExecution = years.size() == 1 && days.size() == 1;
        var output = jsonOutput ? new JsonOutput() : new PrettyPrintOutput();

        if (singleExecution) {
            this.singleSolutionExecutor.setOutput(output);

            String manualInput = null;

            if (inputFromStdin) {
                manualInput = readFromStdin();
            }

            this.singleSolutionExecutor.execute(
                years.getFirst(),
                days.getFirst(),
                test,
                validate,
                overwriteCache,
                manualInput
            );

            output.flush();

            return;
        }

        if (inputFromStdin) {
            throw new CliException("Reading input from stdin is not supported for multiple years or days");
        }

        if (jsonOutput) {
            throw new CliException("JSON output is not supported for multiple years or days");
        }

        this.multiSolutionExecutor.execute(years, days, test, validate, overwriteCache);
        output.flush();
    }

    private List<Integer> resolveDays() {
        var currentYear = year != 0 ? year : LocalDate.now().getYear();
        var maxDay = AoCConfig.getMaxChallengesForYear(currentYear);
        var availableDays = IntStream.rangeClosed(1, maxDay).boxed().toList();

        if (day != 0) {
            if (!AoCConfig.isValidDayForYear(currentYear, day)) {
                throw new CliException(String.format("Invalid day given for year %d, must be between 1 and %d", currentYear, maxDay));
            }

            return List.of(day);
        }

        return availableDays;
    }

    private List<Integer> resolveYears() {
        var availableYears = releasedEvents();

        if (year != 0) {
            if (!availableYears.contains(year)) {
                throw new CliException("Invalid year given, must be one of " + availableYears);
            }

            return List.of(year);
        }

        return availableYears;
    }

    /**
     * List of years for which we have released events, starting from 2015 and ending with either the current year if
     * we're in December or the previous year if we're not.
     */
    private @NotNull List<Integer> releasedEvents() {
        var years = new LinkedList<Integer>();

        for (var i = 2015; i <= LocalDate.now().getYear(); i++) {
            years.add(i);
        }

        if (LocalDate.now().getMonth().getValue() < 12) {
            years.removeLast();
        }

        return years;
    }

    private String readFromStdin() {
        try (var reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new CliException("Failed to read from stdin: " + e.getMessage());
        }
    }
}
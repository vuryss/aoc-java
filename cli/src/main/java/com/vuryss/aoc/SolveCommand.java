package com.vuryss.aoc;

import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
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

        if (singleExecution) {
            this.singleSolutionExecutor.execute(years.getFirst(), days.getFirst(), test, validate, overwriteCache);

            return;
        }

        this.multiSolutionExecutor.execute(years, days, test, validate, overwriteCache);
    }

    private List<Integer> resolveDays() {
        var availableDays = IntStream.rangeClosed(1, 25).boxed().toList();

        if (day != 0) {
            if (!availableDays.contains(day)) {
                throw new RuntimeException("Invalid day given");
            }

            return List.of(day);
        }

        return availableDays;
    }

    private List<Integer> resolveYears() {
        var availableYears = releasedEvents();

        if (year != 0) {
            if (!availableYears.contains(year)) {
                throw new RuntimeException("Invalid year given");
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
}
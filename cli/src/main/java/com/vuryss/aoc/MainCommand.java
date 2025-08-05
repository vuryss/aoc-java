package com.vuryss.aoc;

import com.vuryss.aoc.client.AdventOfCodeClient;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@CommandLine.Command(name = "aoc", mixinStandardHelpOptions = true)
public class MainCommand implements Runnable {
    @CommandLine.Option(names = {"-y", "--year"}, description = "Year of the challenge")
    int year;

    @CommandLine.Option(names = {"-d", "--day"}, description = "Day of the challenge")
    int day;

    @CommandLine.Option(names = {"-t", "--test"}, description = "Use test data")
    boolean test = false;

    @CommandLine.Option(names = {"-v", "--validate"}, description = "Validate answers of already completed puzzles")
    boolean validate = false;

    private final AdventOfCodeClient adventOfCodeClient;

    public MainCommand(AdventOfCodeClient adventOfCodeClient) {
        this.adventOfCodeClient = adventOfCodeClient;
    }

    @Override
    public void run() {
//        if (validate) {
//            var years = resolveYears();
//            var days = resolveDays();
//
//            for (var year: years) {
//                for (var day: days) {
//                    new GameValidator(year, day, test);
//                }
//            }
//
//            return 0;
//        }
//
//        year = resolveYear();
//        day = resolveDay();
//
//        System.out.println("Solving for year " + year + " day " + day + (test ? " with test data" : ""));
//
//        new Game(year, day, test);

        var years = resolveYears();
        var days = resolveDays();
        var singleExecution = years.size() == 1 && days.size() == 1;

        if (singleExecution) {
            System.out.println("Single execution");

            return;
        }

        System.out.println("Multiple executions");
        System.out.println(adventOfCodeClient.downloadInput(2016, 1));

        for (var year: years) {
            for (var day: days) {
                var solution = SolutionRepository.getSolutionFor(year, day);
                var part1Success = true;
                var part2Success = true;

                if (solution.isEmpty()) {
                    System.out.println("Skipping year " + year + " day " + day + " as no solution is available");
                    continue;
                }

                if (test) {
                    for (var testSet : solution.get().part1Tests().entrySet()) {
                        part1Success &= solution.get().part1Solution(testSet.getKey()).equals(testSet.getValue());
                    }

                    for (var testSet : solution.get().part2Tests().entrySet()) {
                        part2Success &= solution.get().part2Solution(testSet.getKey()).equals(testSet.getValue());
                    }

                    System.out.println("Testing year " + year + " day " + day + " part 1: " + (part1Success ? "success" : "failure") + " | part 2: " + (part2Success ? "success" : "failure"));
                    continue;
                }

                if (validate) {
                    var input = "";
                }
            }
        }
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
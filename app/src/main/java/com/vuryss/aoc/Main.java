package com.vuryss.aoc;

import io.github.cdimascio.dotenv.Dotenv;
import picocli.CommandLine;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "aoc", mixinStandardHelpOptions = true)
public class Main implements Callable<Integer> {
    @CommandLine.Option(names = {"-y", "--year"}, description = "Year of the challenge")
    int year;

    @CommandLine.Option(names = {"-d", "--day"}, description = "Day of the challenge")
    int day;

    @CommandLine.Option(names = {"-t", "--test"}, description = "Use test data")
    boolean test = false;

    @CommandLine.Option(names = {"-v", "--validate"}, description = "Validate answers of already completed puzzles")
    boolean validate = false;

    public static void main(String[] args) {
        var exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        if (validate) {
            var years = resolveYears();
            var days = resolveDays();

            for (var year: years) {
                for (var day: days) {
                    new GameValidator(year, day, test);
                }
            }

            return 0;
        }

        year = resolveYear();
        day = resolveDay();

        System.out.println("Solving for year " + year + " day " + day + (test ? " with test data" : ""));

        new Game(year, day, test);

        return 0;
    }

    private List<Integer> resolveYears() {
        if (year != 0) {
            return List.of(resolveYear());
        }

        var years = new LinkedList<Integer>();

        for (var i = 2015; i <= LocalDate.now().getYear(); i++) {
            years.add(i);
        }

        return years;
    }

    private List<Integer> resolveDays() {
        if (day != 0) {
            return List.of(resolveDay());
        }

        var days = new LinkedList<Integer>();

        for (var i = 1; i <= 25; i++) {
            days.add(i);
        }

        return days;
    }

    private int resolveYear() {
        // If year is given - use it if it's valid
        if (year >= 2015 && year <= LocalDate.now().getYear()) {
            return year;
        }

        if (year != 0) {
            throw new RuntimeException("Invalid year given");
        }

        // If we're not in December yet - fallback to previous year, challenges for the current one are not published
        if (LocalDate.now().getMonth().getValue() < 12) {
            return LocalDate.now().getYear() - 1;
        }

        // Use current year (we're in December - actively solving).
        return LocalDate.now().getYear();
    }

    private int resolveDay() {
        // If day is given - use it if it's valid
        if (day >= 1 && day <= 25) {
            return day;
        }

        if (day != 0) {
            throw new RuntimeException("Invalid day given");
        }

        // If we're in December and day is less or equal than 25 - use current day
        if (LocalDate.now().getMonth().getValue() == 12 && LocalDate.now().getDayOfMonth() <= 25) {
            return LocalDate.now().getDayOfMonth();
        }

        // Otherwise - use 1st day of the month
        return 1;
    }
}
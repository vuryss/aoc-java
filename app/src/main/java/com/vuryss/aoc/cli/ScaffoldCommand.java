package com.vuryss.aoc.cli;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.ReactiveAdventOfCodeClient;
import com.vuryss.aoc.solutions.SolutionScaffolding;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "scaffold", description = "Scaffold a new solution class for a given year and day")
public class ScaffoldCommand implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "Puzzle event year, if omitted, counts as all supported events: ${COMPLETION-CANDIDATES}",
        arity = "1",
        converter = EventConverter.class,
        completionCandidates = SupportedEvents.class
    )
    Integer event;

    @CommandLine.Parameters(
        index = "1",
        description = "Puzzle day, if omitted, counts as all days (up to 25, for recent years - less)",
        arity = "1",
        converter = DayConverter.class,
        completionCandidates = SupportedDays.class
    )
    Integer day;

    @CommandLine.Option(
        names = "--source-root",
        required = true,
        paramLabel = "DIRECTORY",
        description = "Java source directory for generated solution classes"
    )
    Path sourceRoot;

    @Override
    public void run() {
        var scaffolder = new SolutionScaffolding(ReactiveAdventOfCodeClient.instance(), sourceRoot);
        var printer = new PrettyPrintConsoleNotificationsOutput();

        printer.announce("Scaffolding event %s, day %s".formatted(event, day));

        scaffolder.scaffold(new PuzzleDayReference(event, day));
    }
}

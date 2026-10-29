package com.vuryss.aoc.cli;

import com.vuryss.aoc.*;
import com.vuryss.aoc.cache.FileCache;
import com.vuryss.aoc.cli.state.DayState;
import com.vuryss.aoc.cli.state.EventState;
import com.vuryss.aoc.cli.display.RichTerminalOutput;
import com.vuryss.aoc.cli.json.JsonOutput;
import com.vuryss.aoc.client.AdventOfCodeClient;
import com.vuryss.aoc.client.CachedAdventOfCodeClient;
import com.vuryss.aoc.client.ReactiveAdventOfCodeClient;
import com.vuryss.aoc.data.DataProviderInterface;
import com.vuryss.aoc.data.ManualDataProvider;
import com.vuryss.aoc.data.UserDataProvider;
import lombok.SneakyThrows;
import picocli.CommandLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CommandLine.Command(name = "solve", mixinStandardHelpOptions = true)
public class SolveCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec;

    @CommandLine.Parameters(
        index = "0",
        description = "Puzzle event year, if omitted, counts as all supported events: ${COMPLETION-CANDIDATES}",
        arity = "0..1",
        converter = EventConverter.class,
        completionCandidates = SupportedEvents.class
    )
    Integer event;

    @CommandLine.Parameters(
        index = "1",
        description = "Puzzle day, if omitted, counts as all days (up to 25, for recent years - less)",
        arity = "0..1",
        converter = DayConverter.class,
        completionCandidates = SupportedDays.class
    )
    Integer day;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "0..1")
    InputSelection inputSelection;

    @CommandLine.Option(
        names = {"-j", "--json-output"},
        description = "Print results as JSON without the terminal display"
    )
    boolean jsonOutput;

    @CommandLine.Option(
        names = {"--stacktrace"},
        description = "Prints all exceptions stack traces"
    )
    boolean printStacktrace;

    @SneakyThrows
    @Override
    public void run() {
        try (var runtime = AppRuntime.create()) {
            solve(runtime);
        }
    }

    public void solve(AppRuntime runtime) throws Exception {
        var events = event == null ? Configuration.getSupportedEvents() : List.of(event);
        var inputSelection = inputSelection();
        var manualInput = inputSelection.manual == null ? null : inputSelection.manual.resolveInput();
        var validate = inputSelection.user != null && inputSelection.user.validate;
        var dataProvider = dataProvider(manualInput);
        OutputInterface output;

        if (printStacktrace) Configuration.enableExceptionStacktrace();

        if (canUseRichTerminal()) {
            output = new RichTerminalOutput(runtime);

            // Restore cursor on ctrl+c in the middle of execution
            var terminalOut = System.out; // To avoid being captured from the stdout overwrite in the RichTerminalOutput

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                terminalOut.print("\u001B[?25h");
                terminalOut.flush();
            }));
        } else {
            output = new JsonOutput(runtime);
        }

        var runner = new Runner(runtime.solver(), runtime.notifications());
        Mono<Void> solving;

        if (events.size() == 1 && day != null) {
            var puzzle = new PuzzleDayReference(events.getFirst(), day);
            var maxChallenges = Configuration.getMaxChallengesForYear(puzzle.event());

            if (puzzle.day() > maxChallenges) {
                throw new CommandLine.ParameterException(
                    commandSpec.commandLine(),
                    "Invalid puzzle day '%d' for event '%d': valid days are 1-%d".formatted(
                        puzzle.day(), puzzle.event(), maxChallenges
                    )
                );
            }

            if (inputSelection.test) {
                var testExecutor = new SingleSolutionTestExecutor(runtime.solver());
                var initial = testExecutor.queued(puzzle);
                solving = output.singleDayTests(puzzle, initial, testExecutor.execute(puzzle, initial));
            } else {
                var updates = runner.run(Flux.just(puzzle), dataProvider, validate);
                solving = output.singleDay(puzzle, DayState.queued(validate), updates);
            }
        } else {
            solving = Flux.fromIterable(events)
                .concatMap(selectedEvent -> {
                    var initial = EventState.queued(
                        Configuration.getMaxChallengesForYear(selectedEvent),
                        validate || inputSelection.test
                    );
                    var puzzles = Flux.range(1, Configuration.getMaxChallengesForYear(selectedEvent))
                        .map(d -> new PuzzleDayReference(selectedEvent, d));
                    var updates = inputSelection.test
                        ? runner.runTests(puzzles)
                        : runner.run(puzzles, dataProvider, validate);

                    return output.wholeEvent(selectedEvent, inputSelection.test, initial, updates);
                })
                .then();
        }

        solving.block();
    }

    private boolean canUseRichTerminal() {
        var console = System.console();

        return !jsonOutput && console != null && console.isTerminal();
    }

    private AdventOfCodeClient adventOfCodeClient(boolean freshData, boolean noCache) {
        var client = ReactiveAdventOfCodeClient.instance();

        if (noCache) return client;

        return new CachedAdventOfCodeClient(client, new FileCache(), freshData);
    }

    private InputSelection inputSelection() {
        if (inputSelection == null) {
            inputSelection = new InputSelection();
            inputSelection.user = new UserInput();
        }

        return inputSelection;
    }

    private DataProviderInterface dataProvider(String manualInput) {
        var inputSelection = inputSelection();

        if (inputSelection.manual != null) {
            return new ManualDataProvider(manualInput);
        }

        if (inputSelection.user != null) {
            return new UserDataProvider(
                adventOfCodeClient(inputSelection.user.freshData, inputSelection.user.noCache)
            );
        }

        return null;
    }

    static class InputSelection {
        @CommandLine.Option(
            names = { "-t", "--input-test"},
            description = "Use test input data instead of real input data"
        )
        boolean test;

        @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
        UserInput user;

        @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
        ManualInput manual;

    }

    static class UserInput {
        @CommandLine.Option(
            names = "--input-user",
            description = "Use authenticated user input data for the challenges"
        )
        boolean selected;

        @CommandLine.Option(
            names = "--fresh-data",
            description = "Force the download of new input data, ignoring any existing cache"
        )
        boolean freshData;

        @CommandLine.Option(
            names = "--no-cache",
            description = "Do not use cached input data, even if available. Also do not write to cache."
        )
        boolean noCache;

        @CommandLine.Option(
            names = { "-v", "--validate" },
            description = "Validate the answers against authenticated user who solved the challenge and has answers."
        )
        boolean validate;
    }

    @CommandLine.Command
    static class ManualInput {
        @CommandLine.Spec
        CommandLine.Model.CommandSpec commandSpec;

        @CommandLine.Option(
            names = {"-i", "--input-manual"},
            required = true,
            description = "Manually enter input data for the challenge."
        )
        boolean selected;

        @CommandLine.Option(
            names = "--input",
            paramLabel = "INPUT",
            description = "Manual input; if omitted, read from stdin"
        )
        String input;

        String resolveInput() throws IOException {
            var command = (SolveCommand) commandSpec.userObject();
            var commandLine = commandSpec.commandLine();

            if (command.event == null || command.day == null || !Configuration.isValidDayForYear(command.event, command.day)) {
                throw new CommandLine.ParameterException(commandLine, "Manual input requires a valid year and day");
            }

            var value = input != null
                ? input
                : new String(System.in.readAllBytes(), StandardCharsets.UTF_8);

            if (value.isEmpty()) {
                throw new CommandLine.ParameterException(commandLine, "Puzzle input is empty");
            }

            return value;
        }
    }
}

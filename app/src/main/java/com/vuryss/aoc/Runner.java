package com.vuryss.aoc;

import com.vuryss.aoc.cli.display.PartState;
import com.vuryss.aoc.cli.display.StateUpdate;
import com.vuryss.aoc.cli.display.TestCaseState;
import com.vuryss.aoc.data.DataProviderInterface;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class Runner {
    private final Scheduler solverScheduler;

    public Flux<StateUpdate> run(Flux<PuzzleDayReference> puzzles, DataProviderInterface dataProvider, boolean validate) {
        var executor = new SingleSolutionExecutor(dataProvider, solverScheduler);

        return runDays(puzzles, puzzle -> executor.executeUpdates(puzzle, validate));
    }

    public Flux<StateUpdate> runTests(Flux<PuzzleDayReference> puzzles) {
        var executor = new SingleSolutionTestExecutor(solverScheduler);

        return runDays(puzzles, puzzle ->
            executor.execute(puzzle).flatMap(tests ->
                puzzle.parts()
                    .map(part -> new StateUpdate(part, _ -> summarize(tests, part.part()))))
        );
    }

    private Flux<StateUpdate> runDays(
        Flux<PuzzleDayReference> puzzles,
        Function<PuzzleDayReference, Flux<StateUpdate>> executeDay
    ) {
        return puzzles.flatMap(puzzle -> runDay(puzzle, executeDay));
    }

    private Flux<StateUpdate> runDay(
        PuzzleDayReference puzzle,
        Function<PuzzleDayReference, Flux<StateUpdate>> executeDay
    ) {
        return Flux
            .defer(() -> executeDay.apply(puzzle))
            .onErrorResume(failure -> {
                var failedAtNs = System.nanoTime();

                return puzzle.parts()
                    .map(part -> new StateUpdate(
                        part,
                        state -> state.isFinished() ? state : state.failed(failure, failedAtNs)
                    ));
            });
    }

    private PartState summarize(List<TestCaseState> tests, int part) {
        var states = tests.stream().filter(test -> test.part() == part).map(TestCaseState::state).toList();
        long startedAtNs = states.stream().mapToLong(PartState::startedAtNs).filter(t -> t >= 0).min().orElse(-1);
        var summary = PartState.queued(true);

        if (startedAtNs >= 0) summary = summary.started(startedAtNs);
        if (!states.stream().allMatch(PartState::isFinished)) return summary;

        long passed = states.stream()
            .filter(state -> state.validationStatus() == PartState.ValidationStatus.VALID)
            .count();
        var failure = states.stream()
            .filter(state -> state.validationStatus() == PartState.ValidationStatus.ERROR)
            .findFirst();

        var validation = PartState.ValidationStatus.VALID;

        if (states.isEmpty()) validation = PartState.ValidationStatus.UNAVAILABLE;
        else if (passed != states.size()) validation = PartState.ValidationStatus.INVALID;

        long finishedAtNs = states.stream().mapToLong(PartState::finishedAtNs).max().orElse(-1);

        var completed = summary.completed(passed + "/" + states.size(), finishedAtNs);

        return failure
            .map(test -> completed.validationFailed(test.failure()))
            .orElse(completed.withValidation(validation, null, null));
    }
}

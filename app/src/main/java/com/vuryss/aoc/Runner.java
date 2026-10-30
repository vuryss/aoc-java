package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationsOutputInterface;
import com.vuryss.aoc.cli.state.DayTestsState;
import com.vuryss.aoc.cli.state.PartState;
import com.vuryss.aoc.cli.state.StateUpdate;
import com.vuryss.aoc.data.DataProviderInterface;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.function.Function;

@RequiredArgsConstructor
public class Runner {
    private final Scheduler solverScheduler;
    private final NotificationsOutputInterface notifications;

    public Flux<StateUpdate> run(Flux<PuzzleDayReference> puzzles, DataProviderInterface dataProvider, boolean validate) {
        var executor = new SingleSolutionExecutor(dataProvider, solverScheduler, notifications);

        return runDays(puzzles, puzzle -> executor.executeUpdates(puzzle, validate));
    }

    public Flux<StateUpdate> runTests(Flux<PuzzleDayReference> puzzles) {
        var executor = new SingleSolutionTestExecutor(solverScheduler);

        return runDays(puzzles, puzzle -> {
            var initial = executor.queued(puzzle);

            return executor.execute(puzzle, initial)
                .scan(initial, DayTestsState::withUpdate)
                .flatMap(state ->
                    puzzle.parts()
                        .map(part -> new StateUpdate(part, _ -> state.summary(part.part())))
                );
        });
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
            .onErrorResume(failure -> failUnfinished(puzzle, failure))
            .concatWith(Flux.defer(() -> failUnfinished(puzzle, new IllegalStateException("Ended without a result"))));
    }

    private Flux<StateUpdate> failUnfinished(PuzzleDayReference puzzle, Throwable failure) {
        var failedAtNs = System.nanoTime();

        return puzzle.parts()
            .map(part -> new StateUpdate(
                part,
                state -> {
                    if (state.isFinished()) return state;
                    if (state.status() == PartState.Status.COMPLETED) return state.validationFailed(failure);

                    return state.failed(failure, failedAtNs);
                }
            ));
    }
}

package com.vuryss.aoc;

import com.vuryss.aoc.cli.state.DayTestsState;
import com.vuryss.aoc.cli.state.PartState;
import com.vuryss.aoc.cli.state.TestCaseState;
import com.vuryss.aoc.cli.state.TestUpdate;
import com.vuryss.aoc.solutions.SolutionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;

@RequiredArgsConstructor
public class SingleSolutionTestExecutor {
    private final SolutionRepository solutionRepository = SolutionRepository.instance();
    private final Scheduler solverScheduler;

    public Flux<TestUpdate> execute(PuzzleDayReference puzzle, DayTestsState initial) {
        var tests = initial.tests();

        return Flux.range(0, tests.size())
            .flatMap(index -> runTest(puzzle, tests.get(index)).map(state -> new TestUpdate(index, state)));
    }

    private Flux<PartState> runTest(PuzzleDayReference puzzle, TestCaseState test) {
        return Flux.defer(() -> {
            var started = test.state().started(System.nanoTime());

            return Flux.concat(
                Mono.just(started),

                Mono.fromCallable(() -> {
                    var solution = solutionRepository.find(puzzle);
                    var result = solution.solve(test.part(), test.input(), true);
                    var validation = test.expected().equals(result)
                        ? PartState.ValidationStatus.VALID
                        : PartState.ValidationStatus.INVALID;

                    return started
                        .completed(result, System.nanoTime())
                        .withValidation(validation, test.expected(), null);
                }).onErrorResume(failure -> Mono.just(
                    started
                        .failed(failure, System.nanoTime())
                        .withValidation(
                            PartState.ValidationStatus.ERROR,
                            test.expected(),
                            failure.toString()
                        )
                ))
            );
        }).subscribeOn(solverScheduler);
    }

    public DayTestsState queued(PuzzleDayReference puzzle) {
        var solution = solutionRepository.find(puzzle);

        if (solution == null) {
            throw new IllegalStateException("No solution found for event " + puzzle.event() + " day " + puzzle.day());
        }

        var cases = new ArrayList<TestCaseState>();

        for (int part = 1; part <= 2; part++) {
            var tests = part == 1 ? solution.part1Tests() : solution.part2Tests();
            int number = 0;

            for (var test: tests.entrySet()) {
                cases.add(new TestCaseState(
                    part,
                    ++number,
                    test.getKey(),
                    test.getValue(),
                    PartState.queued(true)
                ));
            }
        }

        return new DayTestsState(cases);
    }
}

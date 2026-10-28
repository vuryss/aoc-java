package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationsOutputInterface;
import com.vuryss.aoc.cli.display.PartState;
import com.vuryss.aoc.cli.display.TestCaseState;
import com.vuryss.aoc.solutions.SolutionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SingleSolutionTestExecutor {
    private final SolutionRepository solutionRepository = SolutionRepository.instance();
    private final Scheduler solverScheduler;
    private final NotificationsOutputInterface notificationOutput;

    public Flux<List<TestCaseState>> execute(PuzzleDayReference puzzle) {
        return Flux.defer(() -> {
            var testsStates = initialTestCasesStates(puzzle);

            return Flux.fromIterable(testsStates)
                .index()
                .flatMap(indexed ->
                    runTest(puzzle, indexed.getT2()).map(state -> Tuples.of(indexed.getT1().intValue(), state))
                )
                .scan(testsStates, (current, change) -> {
                    var copy = new ArrayList<>(current);
                    int index = change.getT1();
                    copy.set(index, copy.get(index).withState(change.getT2()));
                    return copy;
                });
        });
    }

    private Flux<PartState> runTest(PuzzleDayReference puzzle, TestCaseState test) {
        return Flux.defer(() -> {
            var started = test.state().started(System.nanoTime());

            return Flux.concat(
                Mono.just(started),

                Mono.fromCallable(() -> {
                    var solution = solutionRepository.find(puzzle);
                    var result = test.part() == 1
                        ? solution.part1Solution(test.input(), true)
                        : solution.part2Solution(test.input(), true);
                    var validation = test.expected().equals(result)
                        ? PartState.ValidationStatus.VALID
                        : PartState.ValidationStatus.INVALID;

                    return started
                        .completed(result, System.nanoTime())
                        .withValidation(validation, test.expected(), null);
                }).onErrorResume(failure -> {
                    notificationOutput.exception(failure);

                    return Mono.just(
                        started
                            .failed(failure, System.nanoTime())
                            .withValidation(
                                PartState.ValidationStatus.ERROR,
                                test.expected(),
                                failure.toString()
                            )
                    );
                })
            );
        }).subscribeOn(solverScheduler);
    }

    private List<TestCaseState> initialTestCasesStates(PuzzleDayReference puzzle) {
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

        return cases;
    }
}

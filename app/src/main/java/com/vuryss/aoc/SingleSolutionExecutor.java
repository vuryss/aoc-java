package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationsOutputInterface;
import com.vuryss.aoc.cli.display.StateUpdate;
import com.vuryss.aoc.cli.display.PartState;
import com.vuryss.aoc.client.Answers;
import com.vuryss.aoc.client.Status;
import com.vuryss.aoc.data.DataProviderInterface;
import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.solutions.SolutionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.util.context.Context;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class SingleSolutionExecutor {
    private final SolutionRepository solutionRepository = SolutionRepository.instance();
    private final DataProviderInterface dataProvider;
    private final Scheduler solverScheduler;
    private final NotificationsOutputInterface notificationsOutput;

    public Flux<StateUpdate> executeUpdates(PuzzleDayReference puzzle, boolean validate) {
        return Flux.defer(() -> {
            var input = SharedDownload.of(() -> dataProvider.getInput(puzzle));
            var answers = SharedDownload.of(() -> dataProvider.getAnswers(puzzle));

            return Flux.just(1, 2)
                .flatMap(part -> runPart(new PuzzlePartReference(puzzle, part), input, answers, validate));
        });
    }

    private Flux<StateUpdate> runPart(
        PuzzlePartReference part,
        SharedDownload<String> input,
        SharedDownload<Answers> answers,
        boolean validate
    ) {
        return Flux
            .defer(() -> {
                var solution = solutionRepository.find(part.puzzle());

                if (solution == null) {
                    return Flux.error(
                        new IllegalStateException("No solution found for event " + part.event() + " day " + part.day())
                    );
                }

                Flux<StateUpdate> loading = input.status()
                    .filter(status -> status == Status.DOWNLOADING)
                    .map(_ -> new StateUpdate(part, state -> state.withStatus(PartState.Status.LOADING_INPUT)));

                var process = input
                    .value()
                    .flatMapMany(inputValue -> solvePart(solution, part, inputValue, answers, validate));

                return Flux.mergeSequential(loading, process);
            })
            .subscribeOn(solverScheduler)
            .onErrorResume(failure -> {
                long failedAtNs = System.nanoTime();
                notificationsOutput.exception(failure);

                return Mono.just(new StateUpdate(part, state -> state.failed(failure, failedAtNs)));
            });
    }

    private Flux<StateUpdate> solvePart(
        SolutionInterface solution,
        PuzzlePartReference part,
        String input,
        SharedDownload<Answers> answers,
        boolean validate
    ) {
        return Flux
            .defer(() -> {
                long startedAtNs = System.nanoTime();

                Mono<Solved> calculation = Mono.fromCallable(() -> {
                    try {
                        String result = switch (part.part()) {
                            case 1 -> solution.part1Solution(input, false);
                            case 2 -> solution.part2Solution(input, false);
                            default -> throw new IllegalStateException("Invalid part: " + part.part());
                        };

                        Objects.requireNonNull(result);

                        return new Solved(result, System.nanoTime());
                    } catch (VirtualMachineError e) {
                        throw new RuntimeException(e);
                    }
                });

                return Flux.concat(
                    Mono.just(new StateUpdate(part, state -> state.started(startedAtNs))),

                    calculation.flatMapMany(solved ->
                        Flux.concat(
                            Mono.just(new StateUpdate(part, state -> state.completed(solved.result(), solved.finishedAtNs()))),
                            validate ? validatePart(part, solved.result(), answers) : Flux.empty()
                        )
                    )
                );
            })
            .subscribeOn(solverScheduler);
    }

    private Flux<StateUpdate> validatePart(PuzzlePartReference part, String result, SharedDownload<Answers> answers) {
        return Flux.defer(() -> {
            Flux<StateUpdate> loading = answers.status()
                .filter(status -> status == Status.DOWNLOADING)
                .map(_ -> new StateUpdate(
                    part,
                    state -> state.withValidation(PartState.ValidationStatus.LOADING_EXPECTED_RESULT, null, null)
                ));

            var validation = answers
                .value()
                .map(ans -> {
                    var answer = part.part() == 1 ? ans.part1() : ans.part2();
                    var validationStatus = answer == null
                        ? PartState.ValidationStatus.UNAVAILABLE
                        : answer.equals(result) ? PartState.ValidationStatus.VALID : PartState.ValidationStatus.INVALID;

                    return new StateUpdate(
                        part,
                        state -> state.withValidation(validationStatus, answer, null)
                    );
                })
                .onErrorResume(failure -> {
                    notificationsOutput.exception(failure);
                    return Mono.just(new StateUpdate(
                        part,
                        state -> state.withValidation(PartState.ValidationStatus.ERROR, null, failure.getMessage())
                    ));
                });

            return Flux.mergeSequential(loading, validation);
        });
    }

    private record Solved(String result, long finishedAtNs) {}

    private record SharedDownload<T>(Mono<T> value, Flux<Status> status) {
        static <T> SharedDownload<T> of(Supplier<Mono<T>> source) {
            Sinks.Many<Status> status = Sinks.many().replay().latest();
            Consumer<Status> onStatus = status::tryEmitNext;

            Mono<T> value = Mono.defer(source)
                .contextWrite(Context.of(Status.class, onStatus))
                .doFinally(_ -> status.tryEmitComplete())
                .cache();

            return new SharedDownload<>(value, status.asFlux());
        }
    }
}

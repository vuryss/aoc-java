package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.cli.display.DayState;
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

@RequiredArgsConstructor
public class SingleSolutionExecutor {
    private final SolutionRepository solutionRepository = SolutionRepository.instance();
    private final DataProviderInterface dataProvider;
    private final Scheduler solverScheduler;
    private final NotificationOutputInterface notificationsOutput;

    public Flux<DayState> execute(PuzzleDayReference puzzle, boolean validate) {
        return executeUpdates(puzzle, validate)
            .scanWith(() -> DayState.queued(validate), DayState::withUpdate);
    }

    Flux<StateUpdate> executeUpdates(PuzzleDayReference puzzle, boolean validate) {
        return Flux.defer(() -> {
            Mono<String> input = Mono.defer(() -> dataProvider.getInput(puzzle)).cache();
            Mono<Answers> answers = Mono.defer(() -> dataProvider.getAnswers(puzzle)).cache();

            return Flux.just(1, 2)
                .flatMap(part -> runPart(new PuzzlePartReference(puzzle, part), input, answers, validate));
        });
    }

    private Flux<StateUpdate> runPart(
        PuzzlePartReference part,
        Mono<String> input,
        Mono<Answers> answers,
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

                Sinks.Many<StateUpdate> sink = Sinks.many().unicast().onBackpressureBuffer();
                Consumer<Status> statusConsumer = status -> {
                    if (status == Status.DOWNLOADING) {
                        sink.tryEmitNext(new StateUpdate(
                            part,
                            state -> state.withStatus(PartState.Status.LOADING_INPUT)
                        ));
                    }
                };

                var process = input
                    .contextWrite(Context.of(Status.class, statusConsumer))
                    .doOnTerminate(sink::tryEmitComplete)
                    .flatMapMany(inputValue -> solvePart(solution, part, inputValue, answers, validate));

                return Flux.mergeSequential(sink.asFlux(), process);
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
        Mono<Answers> answers,
        boolean validate
    ) {
        return Flux
            .defer(() -> {
                long startedAtNs = System.nanoTime();

                Mono<Solved> calculation = Mono.fromCallable(() -> {
                    String result = switch (part.part()) {
                        case 1 -> solution.part1Solution(input, false);
                        case 2 -> solution.part2Solution(input, false);
                        default -> throw new IllegalStateException("Invalid part: " + part.part());
                    };

                    Objects.requireNonNull(result);

                    return new Solved(result, System.nanoTime());
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

    private Flux<StateUpdate> validatePart(PuzzlePartReference part, String result, Mono<Answers> answers) {
        return Flux.defer(() -> {
            Sinks.Many<StateUpdate> sink = Sinks.many().unicast().onBackpressureBuffer();

            Consumer<Status> statusConsumer = status -> {
                if (status == Status.DOWNLOADING) sink.tryEmitNext(new StateUpdate(
                    part,
                    state -> state.withValidation(PartState.ValidationStatus.LOADING_EXPECTED_RESULT, null, null)
                ));
            };

            var validation = answers
                .contextWrite(Context.of(Status.class, statusConsumer))
                .doOnTerminate(sink::tryEmitComplete)
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

            return Flux.mergeSequential(sink.asFlux(), validation);
        });
    }

    private record Solved(String result, long finishedAtNs) {}
}

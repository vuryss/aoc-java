package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationsOutputInterface;
import com.vuryss.aoc.cli.display.StateUpdate;
import com.vuryss.aoc.cli.display.PartState;
import com.vuryss.aoc.client.Answers;
import com.vuryss.aoc.client.Download;
import com.vuryss.aoc.client.ResponseException;
import com.vuryss.aoc.data.DataProviderInterface;
import com.vuryss.aoc.solutions.SolutionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RequiredArgsConstructor
public class SingleSolutionExecutor {
    private final SolutionRepository solutionRepository = SolutionRepository.instance();
    private final DataProviderInterface dataProvider;
    private final Scheduler solverScheduler;
    private final NotificationsOutputInterface notifications;

    Flux<StateUpdate> executeUpdates(PuzzleDayReference puzzle, boolean validate) {
        return Flux.defer(() -> {
            if (solutionRepository.find(puzzle) == null) {
                return Flux.error(
                    new IllegalStateException("No solution found for event " + puzzle.event() + " day " + puzzle.day())
                );
            }

            var answers = dataProvider.getAnswers(puzzle)
                .doOnNext(download -> {
                    if (download instanceof Download.Started<Answers>) {
                        notifications.notice("Downloading answers for event %s, day %s".formatted(puzzle.event(), puzzle.day()));
                    }
                })
                .doOnError(ResponseException.class, e -> notifications.error("Error response body: " + e.getBody()))
                .cache();

            return dataProvider.getInput(puzzle)
                .doOnNext(download -> {
                    if (download instanceof Download.Started<String>) {
                        notifications.notice("Downloading input for event %s, day %s".formatted(puzzle.event(), puzzle.day()));
                    }
                })
                .doOnError(ResponseException.class, e -> notifications.error("Error response body: " + e.getBody()))
                .concatMap(download ->
                    switch (download) {
                        case Download.Started<String> _ -> puzzle.parts()
                            .map(part ->
                                new StateUpdate(part, state -> state.withStatus(PartState.Status.LOADING_INPUT))
                            );

                        case Download.Done<String>(var inputValue) -> puzzle.parts()
                            .flatMap(part -> solvePart(part, inputValue, answers, validate));
                    }
                );
        });
    }

    private Flux<StateUpdate> solvePart(
        PuzzlePartReference part,
        String input,
        Flux<Download<Answers>> answers,
        boolean validate
    ) {
        return Flux
            .concat(
                Mono.fromSupplier(System::nanoTime)
                    .map(startedAtNs -> new StateUpdate(part, state -> state.started(startedAtNs))),

                Mono
                    .fromCallable(() -> new Solved(
                        solutionRepository.find(part.puzzle()).solve(part.part(), input, false),
                        System.nanoTime()
                    ))
                    .flatMapMany(solved ->
                        Flux.concat(
                            Mono.just(new StateUpdate(part, state -> state.completed(solved.result(), solved.finishedAtNs()))),
                            validate ? validatePart(part, solved.result(), answers) : Flux.empty()
                        )
                    )
            )
            .subscribeOn(solverScheduler)
            .onErrorResume(failure -> {
                long failedAtNs = System.nanoTime();

                return Mono.just(new StateUpdate(part, state -> state.failed(failure, failedAtNs)));
            });
    }

    private Flux<StateUpdate> validatePart(PuzzlePartReference part, String result, Flux<Download<Answers>> answers) {
        return answers
            .map(download -> switch (download) {
                case Download.Started<Answers> _ -> new StateUpdate(
                    part,
                    state -> state.withValidation(PartState.ValidationStatus.LOADING_EXPECTED_RESULT, null, null)
                );

                case Download.Done<Answers>(var ans) -> {
                    var answer = part.part() == 1 ? ans.part1() : ans.part2();
                    var validationStatus = answer == null
                        ? PartState.ValidationStatus.UNAVAILABLE
                        : answer.equals(result) ? PartState.ValidationStatus.VALID : PartState.ValidationStatus.INVALID;

                    yield new StateUpdate(
                        part,
                        state -> state.withValidation(validationStatus, answer, null)
                    );
                }
            })
            .onErrorResume(failure -> Mono.just(new StateUpdate(part, state -> state.validationFailed(failure))));
    }

    private record Solved(String result, long finishedAtNs) {}
}

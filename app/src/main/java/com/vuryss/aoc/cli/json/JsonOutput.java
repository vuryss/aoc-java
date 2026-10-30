package com.vuryss.aoc.cli.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.OutputInterface;
import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.StackTraces;
import com.vuryss.aoc.cli.state.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

@SuppressWarnings("CallingSubscribeInNonBlockingScope")
@RequiredArgsConstructor
public class JsonOutput implements OutputInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppRuntime runtime;

    public Mono<Void> singleDay(PuzzleDayReference puzzle, DayState initial, Flux<StateUpdate> updates) {
        return printWhenFinished(
            updates.reduce(initial, DayState::withUpdate),
            (dayState, output) -> SingleDayOutputDto.from(puzzle, dayState)
                .withErrors(output.getErrors())
                .withNotices(output.getNotices())
        );
    }

    public Mono<Void> singleDayTests(PuzzleDayReference puzzle, DayTestsState initial, Flux<TestUpdate> updates) {
        return printWhenFinished(
            updates.reduce(initial, DayTestsState::withUpdate),
            (state, output) -> SingleDayTestOutputDto.from(puzzle, state.tests())
                .addErrors(output.getErrors())
                .addNotices(output.getNotices())
        );
    }

    public Mono<Void> wholeEvent(int event, boolean isTest, EventState initial, Flux<StateUpdate> updates) {
        return printWhenFinished(
            updates.reduce(initial, EventState::withUpdate),
            (eventState, output) -> {
                var eventOutput = IntStream.range(0, eventState.days().size())
                    .mapToObj(i -> {
                        var dayState = eventState.days().get(i);
                        return WholeEventOutputDto.DayDto.from(new PuzzleDayReference(event, i + 1), dayState);
                    })
                    .toList();

                return WholeEventOutputDto.from(eventOutput)
                    .withErrors(output.getErrors())
                    .withNotices(output.getNotices());
            }
        );
    }

    private <S extends RunState> Mono<Void> printWhenFinished(
        Mono<S> finalState,
        BiFunction<S, InMemoryNotificationsOutput, Object> toDto
    ) {
        var output = new InMemoryNotificationsOutput();

        return Mono.using(
            () -> runtime.notifications().flux().subscribe(output::show),
            _ -> finalState
                .publishOn(runtime.render())
                .doOnNext(state -> StackTraces.print(state.parts()))
                .doOnNext(state -> print(toDto.apply(state, output)))
                .then(),
            Disposable::dispose
        );
    }

    @SneakyThrows
    private void print(Object dto) {
        System.out.println(objectMapper.writeValueAsString(dto));
    }
}

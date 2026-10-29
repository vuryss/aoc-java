package com.vuryss.aoc.cli.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.OutputInterface;
import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.StackTraces;
import com.vuryss.aoc.cli.state.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@RequiredArgsConstructor
public class JsonOutput implements OutputInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppRuntime runtime;

    public Mono<Void> singleDay(PuzzleDayReference puzzle, DayState initial, Flux<StateUpdate> updates) {
        var output = new InMemoryNotificationsOutput();

        return Mono.using(
            () -> runtime.notifications().subscribe(output),
            _ -> updates
                .reduce(initial, DayState::withUpdate)
                .publishOn(runtime.render())
                .doOnNext(dayState -> StackTraces.print(dayState.parts()))
                .doOnNext(dayState -> print(
                    SingleDayOutputDto.from(puzzle, dayState)
                        .withErrors(output.getErrors())
                        .withNotices(output.getNotices())
                ))
                .then()
        );
    }

    public Mono<Void> singleDayTests(PuzzleDayReference puzzle, DayTestsState initial, Flux<TestUpdate> updates) {
        var output = new InMemoryNotificationsOutput();

        return Mono.using(
            () -> runtime.notifications().subscribe(output),
            _ -> updates
                .reduce(initial, DayTestsState::withUpdate)
                .publishOn(runtime.render())
                .doOnNext(state -> StackTraces.print(state.parts()))
                .doOnNext(state -> print(
                    SingleDayTestOutputDto.from(puzzle, state.tests())
                        .addErrors(output.getErrors())
                        .addNotices(output.getNotices())
                ))
                .then()
        );
    }

    public Mono<Void> wholeEvent(int event, boolean isTest, EventState initial, Flux<StateUpdate> updates) {
        var output = new InMemoryNotificationsOutput();

        return Mono.using(
            () -> runtime.notifications().subscribe(output),
            _ -> updates
                .reduce(initial, EventState::withUpdate)
                .publishOn(runtime.render())
                .doOnNext(eventState -> StackTraces.print(eventState.parts()))
                .doOnNext(eventState -> {
                    var eventOutput = IntStream.range(0, eventState.days().size())
                        .mapToObj(i -> {
                            var dayState = eventState.days().get(i);
                            return WholeEventOutputDto.DayDto.from(new PuzzleDayReference(event, i + 1), dayState);
                        })
                        .toList();

                    print(
                        WholeEventOutputDto.from(eventOutput)
                            .withErrors(output.getErrors())
                            .withNotices(output.getNotices())
                    );
                })
                .then()
        );
    }

    @SneakyThrows
    private void print(Object dto) {
        System.out.println(objectMapper.writeValueAsString(dto));
    }
}

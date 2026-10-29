package com.vuryss.aoc;

import com.vuryss.aoc.cli.display.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OutputInterface {
    Mono<Void> singleDay(PuzzleDayReference puzzle, DayState initial, Flux<StateUpdate> updates);
    Mono<Void> singleDayTests(PuzzleDayReference puzzle, DayTestsState initial, Flux<TestUpdate> updates);
    Mono<Void> wholeEvent(int event, boolean isTest, EventState initial, Flux<StateUpdate> updates);
}

package com.vuryss.aoc;

import com.vuryss.aoc.cli.display.DayState;
import com.vuryss.aoc.cli.display.EventState;
import com.vuryss.aoc.cli.display.StateUpdate;
import com.vuryss.aoc.cli.display.TestCaseState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OutputInterface {
    Mono<Void> singleDay(PuzzleDayReference puzzle, DayState initial, Flux<StateUpdate> updates);
    void singleDayTests(PuzzleDayReference puzzle, Flux<List<TestCaseState>> states) throws Exception;
    Mono<Void> wholeEvent(int event, boolean isTest, EventState initial, Flux<StateUpdate> updates);
}

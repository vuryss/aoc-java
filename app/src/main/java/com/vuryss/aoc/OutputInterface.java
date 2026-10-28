package com.vuryss.aoc;

import com.vuryss.aoc.cli.display.DayState;
import com.vuryss.aoc.cli.display.EventState;
import com.vuryss.aoc.cli.display.TestCaseState;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OutputInterface {
    void singleDay(PuzzleDayReference puzzle, Flux<DayState> states) throws Exception;
    void singleDayTests(PuzzleDayReference puzzle, Flux<List<TestCaseState>> states) throws Exception;
    void wholeEvent(int event, boolean isTest, Flux<EventState> states) throws Exception;
}

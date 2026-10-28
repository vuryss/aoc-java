package com.vuryss.aoc.cli.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.display.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class JsonOutput implements OutputInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppRuntime runtime;

    public void singleDay(PuzzleDayReference puzzle, Flux<DayState> states) throws Exception {
        var output = new InMemoryNotificationsOutput();

        try (var _ = runtime.notifications().subscribe(output)) {
            var lastState = states
                .takeUntil(DayState::isFinished)
                .publishOn(runtime.render())
                .blockLast();

            assert lastState != null;

            var outputDto = SingleDayOutputDto.from(puzzle, lastState)
                .withErrors(output.getErrors())
                .withNotices(output.getNotices());

            System.out.println(objectMapper.writeValueAsString(outputDto));
        }
    }

    public void singleDayTests(PuzzleDayReference puzzle, Flux<List<TestCaseState>> states) throws Exception {
        var output = new InMemoryNotificationsOutput();

        try (var _ = runtime.notifications().subscribe(output)) {
            var testStatesList = states
                .takeUntil(tests -> tests.stream().allMatch(test -> test.state().isFinished()))
                .publishOn(runtime.render())
                .blockLast();

            assert testStatesList != null;

            var outputDto = SingleDayTestOutputDto.from(puzzle, testStatesList)
                .addErrors(output.getErrors())
                .addNotices(output.getNotices());

            System.out.println(objectMapper.writeValueAsString(outputDto));
        }
    }

    public void wholeEvent(int event, boolean isTest, Flux<EventState> states) throws Exception {
        var output = new InMemoryNotificationsOutput();

        try (var _ = runtime.notifications().subscribe(output)) {
            var eventState = states
                .takeUntil(EventState::isFinished)
                .onBackpressureLatest()
                .publishOn(runtime.render())
                .blockLast();

            assert eventState != null;
            var eventOutput = IntStream.range(0, eventState.days().size())
                .mapToObj(i -> {
                    var dayState = eventState.days().get(i);
                    return WholeEventOutputDto.DayDto.from(new PuzzleDayReference(event, i + 1), dayState);
                })
                .toList();
            var outputDto = WholeEventOutputDto.from(eventOutput)
                .withErrors(output.getErrors())
                .withNotices(output.getNotices());

            System.out.println(objectMapper.writeValueAsString(outputDto));
        }
    }
}

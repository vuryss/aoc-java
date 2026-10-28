package com.vuryss.aoc.cli.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.display.*;
import com.vuryss.aoc.data.DataProviderInterface;
import lombok.RequiredArgsConstructor;

import java.util.stream.IntStream;

@RequiredArgsConstructor
public class JsonOutput implements OutputInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppRuntime runtime;

    public void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        DataProviderInterface dataProvider
    ) throws Exception {
        var output = new InMemoryNotificationsOutput();

        try (var _ = runtime.notifications().subscribe(output)) {
            if (isTest) {
                var executor = new SingleSolutionTestExecutor(runtime.solver(), runtime.notifications());

                var testStatesList = executor.execute(puzzle)
                    .takeUntil(tests -> tests.stream().allMatch(test -> test.state().isFinished()))
                    .publishOn(runtime.render())
                    .blockLast();

                assert testStatesList != null;

                var outputDto = SingleDayTestOutputDto.from(puzzle, testStatesList)
                    .addErrors(output.getErrors())
                    .addNotices(output.getNotices());

                System.out.println(objectMapper.writeValueAsString(outputDto));

                return;
            }

            var executor = new SingleSolutionExecutor(dataProvider, runtime.solver(), runtime.notifications());

            var lastState = executor.execute(puzzle, validate)
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

    public void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        DataProviderInterface dataProvider
    ) throws Exception {
        var output = new InMemoryNotificationsOutput();

        try (var _ = runtime.notifications().subscribe(output)) {
            var executor = new MultiSolutionExecutor(runtime.solver(), runtime.notifications());
            var execution = isTest
                ? executor.executeTests(event)
                : executor.execute(event, dataProvider, validate);

            var eventState = execution
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

package com.vuryss.aoc.cli.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.cli.display.*;
import com.vuryss.aoc.data.DataProviderInterface;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;
import java.util.stream.IntStream;

public class JsonOutput implements OutputInterface {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        withSchedulers((solverScheduler, renderScheduler) -> {
            var output = new InMemoryNotificationOutputInterface();

            if (isTest) {
                var executor = new SingleSolutionTestExecutor(solverScheduler, output);

                var testStatesList = executor.execute(puzzle)
                    .takeUntil(tests -> tests.stream().allMatch(test -> test.state().isFinished()))
                    .publishOn(renderScheduler)
                    .blockLast();

                assert testStatesList != null;

                var outputDto = SingleDayTestOutputDto.from(puzzle, testStatesList)
                    .addErrors(output.getErrors())
                    .addNotices(output.getNotices());

                System.out.println(objectMapper.writeValueAsString(outputDto));

                return;
            }

            var executor = new SingleSolutionExecutor(generateDataProvider.apply(output), solverScheduler, output);

            var lastState = executor.execute(puzzle, validate)
                .takeUntil(DayState::isFinished)
                .publishOn(renderScheduler)
                .blockLast();

            assert lastState != null;

            var outputDto = SingleDayOutputDto.from(puzzle, lastState)
                .withErrors(output.getErrors())
                .withNotices(output.getNotices());

            System.out.println(objectMapper.writeValueAsString(outputDto));
        });
    }

    public void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        withSchedulers((solverScheduler, renderScheduler) -> {
            var output = new InMemoryNotificationOutputInterface();
            var executor = new MultiSolutionExecutor(solverScheduler, output);

            var execution = isTest
                ? executor.executeTests(event)
                : executor.execute(event, generateDataProvider.apply(output), validate);

            var eventState = execution
                .takeUntil(EventState::isFinished)
                .onBackpressureLatest()
                .publishOn(renderScheduler)
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
        });
    }

    private void withSchedulers(Execution execution) throws Exception {
        Scheduler solverScheduler = Schedulers.newParallel(
            "aoc-solvers",
            Runtime.getRuntime().availableProcessors()
        );
        Scheduler renderScheduler = Schedulers.newSingle("console-renderer");

        try {
            execution.run(solverScheduler, renderScheduler);
        } finally {
            solverScheduler.dispose();
            renderScheduler.dispose();
        }
    }

    private interface Execution {
        void run(Scheduler solverScheduler, Scheduler rendererSchedule) throws Exception;
    }
}

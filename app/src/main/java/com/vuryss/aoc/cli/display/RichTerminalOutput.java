package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.data.DataProviderInterface;
import dev.tamboui.inline.InlineDisplay;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class RichTerminalOutput implements OutputInterface {
    public void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        withSchedulers((solverScheduler, renderScheduler) -> {
            try (
                var display = new SingleDayDisplay(puzzle);
                var output = new DisplayConsoleNotificationOutput(display.display(), renderScheduler);
                var _ = new RedirectedSystemOut(display.display(), renderScheduler)
            ) {
                if (isTest) {
                    var executor = new SingleSolutionTestExecutor(solverScheduler, output);

                    Flux
                        .combineLatest(
                            Flux.interval(Duration.ofMillis(100)).onBackpressureLatest().startWith(0L),
                            executor.execute(puzzle),
                            (_, tests) -> tests
                        )
                        .takeUntil(tests -> tests.stream().allMatch(test -> test.state().isFinished()))
                        .publishOn(renderScheduler)
                        .doOnNext(display::renderTests)
                        .blockLast();

                    return;
                }

                var executor = new SingleSolutionExecutor(generateDataProvider.apply(output), solverScheduler, output);

                Flux
                    .combineLatest(
                        Flux.interval(Duration.ofMillis(100)).onBackpressureLatest().startWith(0L),
                        executor.execute(puzzle, validate),
                        (_, dayState) -> dayState
                    )
                    .takeUntil(DayState::isFinished)
                    .publishOn(renderScheduler)
                    .doOnNext(display::render)
                    .blockLast();
            }
        });
    }

    public void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        withSchedulers((solverScheduler, renderScheduler) -> {
            try (
                var display = new WholeEventDisplay(event, isTest);
                var output = new DisplayConsoleNotificationOutput(display.display(), renderScheduler);
                var _ = new RedirectedSystemOut(display.display(), renderScheduler)
            ) {
                var executor = new MultiSolutionExecutor(solverScheduler, output);
                var execution = isTest
                    ? executor.executeTests(event)
                    : executor.execute(event, generateDataProvider.apply(output), validate);

                Flux
                    .combineLatest(
                        Flux.interval(Duration.ofMillis(100)).onBackpressureLatest().startWith(0L),
                        execution,
                        (_, eventState) -> eventState
                    )
                    .takeUntil(EventState::isFinished)
                    .onBackpressureLatest()
                    .publishOn(renderScheduler)
                    .doOnNext(display::render)
                    .blockLast();
            }
        });
    }

    private void withSchedulers(Execution execution) throws Exception {
        Scheduler solverScheduler = Schedulers.fromExecutorService(
            Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                Thread.ofPlatform().name("aoc-solver-", 0).factory()
            ),
            "aoc-solvers"
        );
        Scheduler renderScheduler = Schedulers.newSingle("cli-display");

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

final class RedirectedSystemOut implements AutoCloseable {
    private final PrintStream originalOut = System.out;
    private final PrintStream forwardedOut;

    public RedirectedSystemOut(InlineDisplay display, Scheduler scheduler) {
        forwardedOut = new PrintStream(new OutputStream() {
            private final ByteArrayOutputStream line = new ByteArrayOutputStream();

            @Override
            public synchronized void write(int i) {
                if (i == '\n') emit();
                else line.write(i);
            }

            @Override
            public synchronized void close() {
                if (line.size() > 0) emit();
            }

            private void emit() {
                var raw = line.toString(originalOut.charset());
                line.reset();

                var text = raw.endsWith("\r") ? raw.substring(0, raw.length() - 1) : raw;

                scheduler.schedule(() -> display.println(text));
            }
        }, true, originalOut.charset());

        System.setOut(forwardedOut);
    }

    @Override
    public void close() {
        System.setOut(originalOut);
        forwardedOut.close();
    }
}
package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.data.DataProviderInterface;
import dev.tamboui.inline.InlineDisplay;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.io.*;
import java.time.Duration;
import java.util.function.Function;

@RequiredArgsConstructor
public class RichTerminalOutput implements OutputInterface {
    private final AppRuntime runtime;

    public void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        try (
            var display = new SingleDayDisplay(puzzle);
            var output = new DisplayConsoleNotificationOutput(display.display(), runtime.render());
            var _ = new RedirectedSystemOut(display.display(), runtime.render())
        ) {
            if (isTest) {
                var executor = new SingleSolutionTestExecutor(runtime.solver(), output);

                Flux
                    .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                    .withLatestFrom(executor.execute(puzzle), (_, state) -> state)
                    .takeUntil(tests -> tests.stream().allMatch(test -> test.state().isFinished()))
                    .doOnNext(display::renderTests)
                    .blockLast();

                return;
            }

            var executor = new SingleSolutionExecutor(generateDataProvider.apply(output), runtime.solver(), output);

            Flux
                .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                .withLatestFrom(executor.execute(puzzle, validate), (_, state) -> state)
                .takeUntil(DayState::isFinished)
                .doOnNext(display::render)
                .blockLast();
        }
    }

    public void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception {
        try (
            var display = new WholeEventDisplay(event, isTest);
            var output = new DisplayConsoleNotificationOutput(display.display(), runtime.render());
            var _ = new RedirectedSystemOut(display.display(), runtime.render())
        ) {
            var executor = new MultiSolutionExecutor(runtime.solver(), output);
            var execution = isTest
                ? executor.executeTests(event)
                : executor.execute(event, generateDataProvider.apply(output), validate);

            Flux
                .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                .withLatestFrom(execution, (_, state) -> state)
                .takeUntil(EventState::isFinished)
                .doOnNext(display::render)
                .blockLast();
        }
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
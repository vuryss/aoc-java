package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.*;
import com.vuryss.aoc.cli.AppRuntime;
import com.vuryss.aoc.cli.StackTraces;
import dev.tamboui.inline.InlineDisplay;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.*;
import java.time.Duration;

@RequiredArgsConstructor
public class RichTerminalOutput implements OutputInterface {
    private final AppRuntime runtime;

    public Mono<Void> singleDay(PuzzleDayReference puzzle, DayState initial, Flux<StateUpdate> updates) {
        return Mono
            .using(
                () -> new SingleDayDisplay(puzzle),
                display -> Mono.using(
                    () -> attach(display.display()),
                    _ -> Flux
                        .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                        .withLatestFrom(updates.scan(initial, DayState::withUpdate), (_, state) -> state)
                        .takeUntil(DayState::isFinished)
                        .doOnNext(display::render)
                        .last()
                ).publishOn(runtime.render())
            )
            .doOnNext(dayState -> StackTraces.print(dayState.parts()))
            .then();
    }

    public Mono<Void> singleDayTests(PuzzleDayReference puzzle, DayTestsState initial, Flux<TestUpdate> updates) {
        return Mono
            .using(
                () -> new SingleDayDisplay(puzzle),
                display -> Mono.using(
                    () -> attach(display.display()),
                    _ -> Flux
                        .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                        .withLatestFrom(updates.scan(initial, DayTestsState::withUpdate), (_, state) -> state)
                        .takeUntil(DayTestsState::isFinished)
                        .doOnNext(state -> display.renderTests(state.tests()))
                        .last()
                ).publishOn(runtime.render())
            )
            .doOnNext(state -> StackTraces.print(state.parts()))
            .then();
    }

    public Mono<Void> wholeEvent(int event, boolean isTest, EventState initial, Flux<StateUpdate> updates) {
        return Mono
            .using(
                () -> new WholeEventDisplay(event, isTest),
                display -> Mono.using(
                    () -> attach(display.display()),
                    _ -> Flux
                        .interval(Duration.ZERO, Duration.ofMillis(100), runtime.render())
                        .withLatestFrom(updates.scan(initial, EventState::withUpdate), (_, state) -> state)
                        .takeUntil(EventState::isFinished)
                        .doOnNext(display::render)
                        .last()
                ).publishOn(runtime.render())
            )
            .doOnNext(eventState -> StackTraces.print(eventState.parts()))
            .then();
    }

    private AutoCloseable attach(InlineDisplay display) {
        var redirectedOut = new RedirectedSystemOut(display, runtime.render());
        var notificationsOutput = new DisplayConsoleNotificationsOutput(display, runtime.render());
        var notifications = runtime.notifications().subscribe(notificationsOutput);

        return () -> {
            notifications.close();
            redirectedOut.close();
        };
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
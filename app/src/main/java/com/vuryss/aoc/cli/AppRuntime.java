package com.vuryss.aoc.cli;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

public record AppRuntime(Scheduler solver, Scheduler render, NotificationsOutput notifications) implements AutoCloseable {
    public static AppRuntime create() {
        var solverThreads = Thread.ofPlatform().name("aoc-solver-", 0).stackSize(512L*1024*1024).factory();
        var solver = Schedulers.fromExecutorService(
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), solverThreads),
            "aoc-solvers"
        );

        return new AppRuntime(solver, Schedulers.newSingle("aoc-render"), new NotificationsOutput());
    }

    @Override
    public void close() {
        solver.dispose();
        render.dispose();
    }
}

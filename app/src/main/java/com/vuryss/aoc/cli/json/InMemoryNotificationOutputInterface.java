package com.vuryss.aoc.cli.json;

import com.vuryss.aoc.Configuration;
import com.vuryss.aoc.cli.NotificationOutputInterface;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryNotificationOutputInterface implements NotificationOutputInterface {
    @Getter
    private final List<String> errors = new CopyOnWriteArrayList<>();

    @Getter
    private final List<String> notices = new CopyOnWriteArrayList<>();

    @Getter
    private final List<String> announces = new CopyOnWriteArrayList<>();

    @Override
    public void error(String message) {
        errors.add(message);
    }

    @Override
    public void announce(String message) {
        announces.add(message);
    }

    @Override
    public void notice(String message) {
        notices.add(message);
    }

    @Override
    public void exception(Throwable e) {
        if (!Configuration.exceptionStacktraceEnabled()) return;

        System.err.println();
        e.printStackTrace(System.err);
        System.err.println();
    }
}

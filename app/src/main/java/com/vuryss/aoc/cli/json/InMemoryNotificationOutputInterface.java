package com.vuryss.aoc.cli.json;

import com.vuryss.aoc.cli.NotificationOutputInterface;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotificationOutputInterface implements NotificationOutputInterface {
    @Getter
    private final List<String> errors = new ArrayList<>();

    @Getter
    private final List<String> notices = new ArrayList<>();

    @Getter
    private final List<String> announces = new ArrayList<>();

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
}

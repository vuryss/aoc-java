package com.vuryss.aoc.cli;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class NotificationsOutput implements NotificationsOutputInterface {
    private final Sinks.Many<Notification> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public void error(String message) {
        emit(new Notification.Error(message));
    }

    @Override
    public void announce(String message) {
        emit(new Notification.Announce(message));
    }

    @Override
    public void notice(String message) {
        emit(new Notification.Notice(message));
    }

    public Flux<Notification> flux() {
        return sink.asFlux();
    }

    private synchronized void emit(Notification notification) {
        sink.tryEmitNext(notification);
    }
}

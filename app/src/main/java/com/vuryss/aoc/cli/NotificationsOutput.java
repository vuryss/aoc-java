package com.vuryss.aoc.cli;

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

    @Override
    public void exception(Throwable e) {
        emit(new Notification.Failure(e));
    }

    public AutoCloseable subscribe(NotificationsOutputInterface output) {
        var subscription = sink.asFlux().subscribe(output::show);

        return subscription::dispose;
    }

    private synchronized void emit(Notification notification) {
        sink.tryEmitNext(notification);
    }
}

package com.vuryss.aoc.cli;

public interface NotificationsOutputInterface {
    void error(String message);
    void announce(String message);
    void notice(String message);

    default void show(Notification notification) {
        switch (notification) {
            case Notification.Error(var message) -> error(message);
            case Notification.Announce(var message) -> announce(message);
            case Notification.Notice(var message) -> notice(message);
        }
    }
}

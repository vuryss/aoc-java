package com.vuryss.aoc.cli;

public interface NotificationOutputInterface {
    void error(String message);
    void announce(String message);
    void notice(String message);
}

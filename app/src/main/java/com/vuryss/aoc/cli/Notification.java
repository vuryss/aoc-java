package com.vuryss.aoc.cli;

public sealed interface Notification {
    record Error(String message) implements Notification {}
    record Announce(String message) implements Notification {}
    record Notice(String message) implements Notification {}
}

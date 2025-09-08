package com.vuryss.aoc.cli;

public record Result(Status status, String error) {
    public enum Status {
        ERROR,
        SUCCESS,
    }
}

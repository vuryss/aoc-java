package com.vuryss.aoc.api;

public class SolutionResult {
    public enum Status {
        ERROR,
        SUCCESS,
    }

    public final Status status;
    public final String error;
    public final String part1;
    public final String part2;

    public SolutionResult(Status status, String error) {
        this.status = status;
        this.error = error;
        this.part1 = null;
        this.part2 = null;
    }

    public SolutionResult(String part1, String part2) {
        this.status = Status.SUCCESS;
        this.error = null;
        this.part1 = part1;
        this.part2 = part2;
    }
}

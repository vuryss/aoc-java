package com.vuryss.aoc.cli.display;

public record PartState(
    Status status,
    String result,
    long startedAtNs,
    long finishedAtNs,
    ValidationStatus validationStatus,
    String expectedResult,
    String error
) {
    public static PartState queued(boolean validate) {
        return new PartState(
            Status.QUEUED,
            null,
            -1,
            -1,
            validate ? ValidationStatus.QUEUED : ValidationStatus.NOT_REQUESTED,
            null,
            null
        );
    }

    public PartState failed(Throwable failure, long failureAtNs) {
        long end = status == Status.RUNNING ? failureAtNs : finishedAtNs;

        return new PartState(
            Status.ERROR,
            null,
            startedAtNs,
            end,
            validationStatus,
            null,
            failure.toString()
        );
    }

    public PartState withStatus(Status nextStatus) {
        return new PartState(
            nextStatus,
            result,
            startedAtNs,
            finishedAtNs,
            validationStatus,
            expectedResult,
            error
        );
    }

    public PartState started(long startedAtNs) {
        return new PartState(
            Status.RUNNING,
            result,
            startedAtNs,
            -1,
            validationStatus,
            expectedResult,
            error
        );
    }

    public PartState completed(String value, long finishedAtNs) {
        return new PartState(
            Status.COMPLETED,
            value,
            startedAtNs,
            finishedAtNs,
            validationStatus,
            expectedResult,
            error
        );
    }

    public PartState withValidation(ValidationStatus nextValidationStatus, String expectedResult, String error) {
        return new PartState(
            status,
            result,
            startedAtNs,
            finishedAtNs,
            nextValidationStatus,
            expectedResult,
            error
        );
    }

    public double elapsedMs() {
        long end = status == Status.RUNNING ? System.nanoTime() : finishedAtNs;
        return (end - startedAtNs) / 1_000_000.0;
    }

    public boolean isFinished() {
        if (status == Status.ERROR) return true;
        if (status != Status.COMPLETED) return false;

        return switch (validationStatus) {
            case NOT_REQUESTED, VALID, INVALID, UNAVAILABLE, ERROR -> true;
            case QUEUED, LOADING_EXPECTED_RESULT -> false;
        };
    }

    public enum Status {
        QUEUED,
        LOADING_INPUT,
        RUNNING,
        COMPLETED,
        ERROR
    }

    public enum ValidationStatus {
        NOT_REQUESTED,
        QUEUED,
        LOADING_EXPECTED_RESULT,
        VALID,
        INVALID,
        UNAVAILABLE,
        ERROR
    }
}


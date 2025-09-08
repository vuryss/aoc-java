package com.vuryss.aoc.api.process;

public class ProcessExecutionException extends RuntimeException {
    public ProcessExecutionException(String message) {
        super(message);
    }

    public ProcessExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}

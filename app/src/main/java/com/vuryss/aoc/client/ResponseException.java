package com.vuryss.aoc.client;

import lombok.Getter;

public class ResponseException extends RuntimeException {
    @Getter
    private final String body;

    public ResponseException(String message, String body) {
        super(message);
        this.body = body;
    }
}

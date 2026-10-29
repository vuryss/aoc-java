package com.vuryss.aoc.cli;

import com.vuryss.aoc.Configuration;
import com.vuryss.aoc.cli.state.PartState;

import java.util.Objects;
import java.util.stream.Stream;

public class StackTraces {
    public static void print(Stream<PartState> states) {
        if (!Configuration.exceptionStacktraceEnabled()) return;

        states.map(PartState::failure)
            .filter(Objects::nonNull)
            .forEach(failure -> failure.printStackTrace(System.err));
    }
}

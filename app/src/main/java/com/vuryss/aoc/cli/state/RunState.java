package com.vuryss.aoc.cli.state;

import java.util.stream.Stream;

public interface RunState {
    boolean isFinished();

    Stream<PartState> parts();
}

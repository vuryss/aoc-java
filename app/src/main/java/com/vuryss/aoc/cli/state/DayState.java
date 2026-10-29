package com.vuryss.aoc.cli.state;

import java.util.stream.Stream;

public record DayState(
    PartState part1,
    PartState part2
) {
    public static DayState queued(boolean validate) {
        return new DayState(
            PartState.queued(validate),
            PartState.queued(validate)
        );
    }

    public DayState withUpdate(StateUpdate change) {
        return switch (change.part().part()) {
            case 1 -> new DayState(change.update().apply(part1), part2);
            case 2 -> new DayState(part1, change.update().apply(part2));
            default -> throw new IllegalArgumentException("Invalid part number: " + change.part());
        };
    }

    public boolean isFinished() {
        return part1.isFinished() && part2.isFinished();
    }

    public Stream<PartState> parts() {
        return Stream.of(part1, part2);
    }
}

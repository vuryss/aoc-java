package com.vuryss.aoc;

import reactor.core.publisher.Flux;

public record PuzzleDayReference(
    int event,
    int day
) {
    public Flux<PuzzlePartReference> parts() {
        return Flux.just(1, 2).map(part -> new PuzzlePartReference(this, part));
    }
}

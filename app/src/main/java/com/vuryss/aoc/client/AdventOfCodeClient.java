package com.vuryss.aoc.client;

import com.vuryss.aoc.PuzzleDayReference;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdventOfCodeClient {
    Flux<Download<String>> getPlayerInput(PuzzleDayReference puzzle);
    Flux<Download<Answers>> getCorrectAnswers(PuzzleDayReference puzzle);
    Mono<String> getDayDescription(PuzzleDayReference puzzle);
}

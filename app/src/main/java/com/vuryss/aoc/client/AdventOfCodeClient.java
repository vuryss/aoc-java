package com.vuryss.aoc.client;

import com.vuryss.aoc.PuzzleDayReference;
import reactor.core.publisher.Mono;

public interface AdventOfCodeClient {
    Mono<String> getPlayerInput(PuzzleDayReference puzzle);
    Mono<Answers> getCorrectAnswers(PuzzleDayReference puzzle);
    Mono<String> getDayDescription(PuzzleDayReference puzzle);
}

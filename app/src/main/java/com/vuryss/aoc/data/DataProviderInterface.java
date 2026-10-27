package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.Answers;
import reactor.core.publisher.Mono;

public interface DataProviderInterface {
    Mono<String> getInput(PuzzleDayReference puzzle);
    Mono<Answers> getAnswers(PuzzleDayReference puzzle);
}

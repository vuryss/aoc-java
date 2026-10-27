package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.Answers;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ManualDataProvider implements DataProviderInterface {
    private final String input;

    @Override
    public Mono<String> getInput(PuzzleDayReference puzzle) {
        return Mono.just(input);
    }

    public Mono<Answers> getAnswers(PuzzleDayReference puzzle) {
        return Mono.error(new RuntimeException("Answers not implemented in manual data input"));
    }
}

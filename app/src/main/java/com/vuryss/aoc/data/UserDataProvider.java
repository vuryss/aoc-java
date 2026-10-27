package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.AdventOfCodeClient;
import com.vuryss.aoc.client.Answers;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserDataProvider implements DataProviderInterface {
    private final AdventOfCodeClient client;

    @Override
    public Mono<String> getInput(PuzzleDayReference puzzle) {
        return client.getPlayerInput(puzzle);
    }

    @Override
    public Mono<Answers> getAnswers(PuzzleDayReference puzzle) {
        return client.getCorrectAnswers(puzzle);
    }

}

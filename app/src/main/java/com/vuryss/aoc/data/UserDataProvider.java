package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.AdventOfCodeClient;
import com.vuryss.aoc.client.Answers;
import com.vuryss.aoc.client.Download;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class UserDataProvider implements DataProviderInterface {
    private final AdventOfCodeClient client;

    @Override
    public Flux<Download<String>> getInput(PuzzleDayReference puzzle) {
        return client.getPlayerInput(puzzle);
    }

    @Override
    public Flux<Download<Answers>> getAnswers(PuzzleDayReference puzzle) {
        return client.getCorrectAnswers(puzzle);
    }

}

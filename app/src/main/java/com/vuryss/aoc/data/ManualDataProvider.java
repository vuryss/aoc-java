package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.Answers;
import com.vuryss.aoc.client.Download;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ManualDataProvider implements DataProviderInterface {
    private final String input;

    @Override
    public Flux<Download<String>> getInput(PuzzleDayReference puzzle) {
        return Flux.just(new Download.Done<>(input));
    }

    public Flux<Download<Answers>> getAnswers(PuzzleDayReference puzzle) {
        return Flux.error(new RuntimeException("Answers not implemented in manual data input"));
    }
}

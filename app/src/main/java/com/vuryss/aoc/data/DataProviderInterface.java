package com.vuryss.aoc.data;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.Answers;
import com.vuryss.aoc.client.Download;
import reactor.core.publisher.Flux;

public interface DataProviderInterface {
    Flux<Download<String>> getInput(PuzzleDayReference puzzle);
    Flux<Download<Answers>> getAnswers(PuzzleDayReference puzzle);
}

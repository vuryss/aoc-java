package com.vuryss.aoc.client;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cache.FileCache;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CachedAdventOfCodeClient implements AdventOfCodeClient {
    private final AdventOfCodeClient adventOfCodeClient;
    private final FileCache fileCacheService;
    private final boolean requireFreshData;

    public CachedAdventOfCodeClient(
        AdventOfCodeClient adventOfCodeClient,
        FileCache fileCacheService,
        boolean requireFreshData
    ) {
        this.adventOfCodeClient = adventOfCodeClient;
        this.fileCacheService = fileCacheService;
        this.requireFreshData = requireFreshData;
    }

    @Override
    public Flux<Download<String>> getPlayerInput(PuzzleDayReference puzzle) {
        return Flux.defer(() -> {
            String cacheKey = String.format("input-%d-%d", puzzle.event(), puzzle.day());

            if (!requireFreshData && fileCacheService.exists(cacheKey)) {
                return Flux.just(new Download.Done<>(fileCacheService.get(cacheKey).orElseThrow()));
            }

            return adventOfCodeClient.getPlayerInput(puzzle)
                .doOnNext(download -> {
                    if (download instanceof Download.Done<String>(var input)) fileCacheService.put(cacheKey, input);
                });
        });
    }

    @Override
    public Flux<Download<Answers>> getCorrectAnswers(PuzzleDayReference puzzle) {
        return Flux.defer(() -> {
            String cacheKeyPart1 = String.format("answer-%d-%d-part%d", puzzle.event(), puzzle.day(), 1);
            String cacheKeyPart2 = String.format("answer-%d-%d-part%d", puzzle.event(), puzzle.day(), 2);

            if (!requireFreshData && fileCacheService.exists(cacheKeyPart1) && fileCacheService.exists(cacheKeyPart2)) {
                String answer1 = fileCacheService.get(cacheKeyPart1).orElseThrow();
                String answer2 = fileCacheService.get(cacheKeyPart2).orElseThrow();

                return Flux.just(new Download.Done<>(new Answers(answer1, answer2)));
            }

            return adventOfCodeClient.getCorrectAnswers(puzzle)
                .doOnNext(download -> {
                    if (download instanceof Download.Done<Answers>(var answers)) {
                        fileCacheService.put(cacheKeyPart1, answers.part1());

                        if (answers.part2() != null)
                            fileCacheService.put(cacheKeyPart2, answers.part2());
                    }
                });
        });
    }

    @Override
    public Mono<String> getDayDescription(PuzzleDayReference puzzle) {
        return Mono.error(new RuntimeException("Caching of description is not implemented."));
    }
}

package com.vuryss.aoc.client;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cache.FileCache;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
        String cacheKey = String.format("input-%d-%d", puzzle.event(), puzzle.day());

        return readCache(cacheKey)
            .<Download<String>>map(Download.Done::new)
            .flux()
            .switchIfEmpty(
                adventOfCodeClient.getPlayerInput(puzzle)
                    .publishOn(Schedulers.boundedElastic())
                    .doOnNext(download -> {
                        if (download instanceof Download.Done<String>(var input)) fileCacheService.put(cacheKey, input);
                    })
            )
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Download<Answers>> getCorrectAnswers(PuzzleDayReference puzzle) {
        String cacheKeyPart1 = String.format("answer-%d-%d-part%d", puzzle.event(), puzzle.day(), 1);
        String cacheKeyPart2 = String.format("answer-%d-%d-part%d", puzzle.event(), puzzle.day(), 2);

        return readCache(cacheKeyPart1)
            .zipWith(readCache(cacheKeyPart2), Answers::new)
            .<Download<Answers>>map(Download.Done::new)
            .flux()
            .switchIfEmpty(
                adventOfCodeClient.getCorrectAnswers(puzzle)
                    .publishOn(Schedulers.boundedElastic())
                    .doOnNext(download -> {
                        if (download instanceof Download.Done<Answers>(var answers)) {
                            fileCacheService.put(cacheKeyPart1, answers.part1());

                            if (answers.part2() != null)
                                fileCacheService.put(cacheKeyPart2, answers.part2());
                        }
                    })
            )
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> getDayDescription(PuzzleDayReference puzzle) {
        return adventOfCodeClient.getDayDescription(puzzle);
    }

    private Mono<String> readCache(String key) {
        if (requireFreshData) return Mono.empty();

        return Mono.fromCallable(() -> fileCacheService.get(key).orElse(null)); // null == empty Mono
    }
}

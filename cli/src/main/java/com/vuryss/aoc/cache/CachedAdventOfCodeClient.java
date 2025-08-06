package com.vuryss.aoc.cache;

import com.vuryss.aoc.client.AdventOfCodeClient;
import com.vuryss.aoc.client.Answers;
import jakarta.enterprise.context.ApplicationScoped;
import picocli.CommandLine;

@ApplicationScoped
public class CachedAdventOfCodeClient {
    private final AdventOfCodeClient adventOfCodeClient;
    private final FileCacheService fileCacheService;

    public CachedAdventOfCodeClient(AdventOfCodeClient adventOfCodeClient, FileCacheService fileCacheService) {
        this.adventOfCodeClient = adventOfCodeClient;
        this.fileCacheService = fileCacheService;
    }

    public String getPlayerInput(int year, int day, boolean overwriteCache) {
        String cacheKey = String.format("input-%d-%d", year, day);

        if (!overwriteCache && fileCacheService.exists(cacheKey)) {
            return fileCacheService.get(cacheKey).orElseThrow();
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "@|bold,yellow Downloading input for year %s day %s|@",
            year,
            day
        )));

        String input = adventOfCodeClient.getPlayerInput(year, day);
        fileCacheService.put(cacheKey, input);

        return input;
    }

    public Answers getCorrectAnswers(int eventYear, int eventDay, boolean overwriteCache) {
        String cacheKeyPart1 = String.format("answer-%d-%d-part%d", eventYear, eventDay, 1);
        String cacheKeyPart2 = String.format("answer-%d-%d-part%d", eventYear, eventDay, 2);

        if (!overwriteCache && fileCacheService.exists(cacheKeyPart1) && fileCacheService.exists(cacheKeyPart2)) {
            String answer1 = fileCacheService.get(cacheKeyPart1).orElseThrow();
            String answer2 = fileCacheService.get(cacheKeyPart2).orElseThrow();

            return new Answers(answer1, answer2);
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "@|bold,yellow Downloading answers for year %s day %s|@",
            eventYear,
            eventDay
        )));

        Answers answers = adventOfCodeClient.getCorrectAnswers(eventYear, eventDay);
        fileCacheService.put(cacheKeyPart1, answers.part1());
        fileCacheService.put(cacheKeyPart2, answers.part2());

        return answers;
    }
}

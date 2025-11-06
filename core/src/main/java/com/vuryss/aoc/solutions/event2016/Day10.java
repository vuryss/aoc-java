package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    private static final Pattern VALUE_PATTERN = Pattern.compile("^value (\\d+) goes to bot (\\d+)$");
    private static final Pattern BOT_INSTRUCTION_PATTERN = Pattern.compile(
        "^bot (?<bot>\\d+) gives low to (?<lowType>bot|output) (?<lowId>\\d+) and high to (?<highType>bot|output) (?<highId>\\d+)$"
    );

    private final Map<Integer, Bot> bots = new HashMap<>();
    private final Map<Integer, Set<Integer>> outputs = new HashMap<>();
    private final Queue<Integer> readyBots = new ArrayDeque<>();

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            value 5 goes to bot 2
            bot 2 gives low to bot 1 and high to bot 0
            value 3 goes to bot 1
            bot 1 gives low to output 1 and high to bot 0
            bot 0 gives low to output 2 and high to output 0
            value 2 goes to bot 2
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        loadBots(input);
        var chip1 = isTest ? 5 : 61;
        var chip2 = isTest ? 2 : 17;

        while (!readyBots.isEmpty()) {
            var botId = readyBots.poll();
            var bot = bots.get(botId);

            if (bot == null || bot.chips.size() != 2) {
                continue;
            }

            if (bot.chips.contains(chip1) && bot.chips.contains(chip2)) {
                return String.valueOf(bot.id);
            }

            bot.proceed();
        }

        return "-not found-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        loadBots(input);

        while (!readyBots.isEmpty()) {
            var bot = bots.get(readyBots.poll());

            if (bot == null || bot.chips.size() != 2) {
                continue;
            }

            bot.proceed();
        }

        return String.valueOf(
            outputs.getOrDefault(0, Set.of(0)).iterator().next() *
            outputs.getOrDefault(1, Set.of(0)).iterator().next() *
            outputs.getOrDefault(2, Set.of(0)).iterator().next()
        );
    }

    private void loadBots(String input) {
        var lines = input.trim().split("\n");
        bots.clear();
        outputs.clear();
        readyBots.clear();

        for (var line : lines) {
            var valueMatcher = VALUE_PATTERN.matcher(line);

            if (valueMatcher.matches()) {
                bots.computeIfAbsent(Integer.parseInt(valueMatcher.group(2)), Bot::new)
                    .addChip(Integer.parseInt(valueMatcher.group(1)));
                continue;
            }

            var botMatcher = BOT_INSTRUCTION_PATTERN.matcher(line);

            if (botMatcher.matches()) {
                var bot = bots.computeIfAbsent(Integer.parseInt(botMatcher.group("bot")), Bot::new);

                bot.low = new Destination(
                    botMatcher.group("lowType").equals("bot") ? DestinationType.BOT : DestinationType.OUTPUT,
                    Integer.parseInt(botMatcher.group("lowId"))
                );

                bot.high = new Destination(
                    botMatcher.group("highType").equals("bot") ? DestinationType.BOT : DestinationType.OUTPUT,
                    Integer.parseInt(botMatcher.group("highId"))
                );
            }
        }
    }

    private class Bot {
        public int id;
        public Set<Integer> chips = new HashSet<>();
        public Destination low;
        public Destination high;

        public Bot(int id) {
            this.id = id;
        }

        public void addChip(int chip) {
            chips.add(chip);

            if (chips.size() == 2) {
                readyBots.offer(id);
            }
        }

        public void proceed() {
            if (chips.size() != 2) {
                return;
            }

            giveChip(low, Collections.min(chips));
            giveChip(high, Collections.max(chips));

            chips.clear();
        }

        private void giveChip(Destination dest, int chip) {
            switch (dest.type()) {
                case BOT -> bots.computeIfAbsent(dest.id(), Bot::new).addChip(chip);
                case OUTPUT -> outputs.computeIfAbsent(dest.id(), k -> new HashSet<>()).add(chip);
            }
        }
    }

    private enum DestinationType { BOT, OUTPUT }
    private record Destination(DestinationType type, int id) {}
}

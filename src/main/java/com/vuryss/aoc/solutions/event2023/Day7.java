package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.StringUtil;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day7 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """,
            "6440"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """,
            "5905"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        long sum = 0L;
        var handsScores = new TreeMap<Hand, Integer>(Hand::compare);
        var place = 1;

        for (var line: lines) {
            var parts = line.split(" ");
            handsScores.put(new Hand(parts[0], false), Integer.parseInt(parts[1]));
        }

        for (var handScore: handsScores.entrySet()) {
            sum += (long) place++ * handScore.getValue();
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        long sum = 0L;
        var handsScores = new TreeMap<Hand, Integer>(Hand::compare);
        var place = 1;

        for (var line: lines) {
            var parts = line.split(" ");
            handsScores.put(new Hand(parts[0], true), Integer.parseInt(parts[1]));
        }

        for (var handScore: handsScores.entrySet()) {
            sum += (long) place++ * handScore.getValue();
        }

        return String.valueOf(sum);
    }

    private static class Hand {
        private final String cardsValue;
        private static final Map<String, Integer> typeValue = Map.of(
            "5", 7,
            "41", 6,
            "32", 5,
            "311", 4,
            "221", 3,
            "2111", 2,
            "11111", 1
        );
        final static String weights = "23456789TJQKA";
        final static String weights2 = "J23456789TQKA";
        final static String replacements = "23456789TQKA";

        public Hand(String cards, boolean adjustForPart2) {
            var value = new StringBuilder();

            for (var card: cards.toCharArray()) {
                value.append(Integer.toString(adjustForPart2 ? weights2.indexOf(card) : weights.indexOf(card), 16));
            }

            if (adjustForPart2 && cards.indexOf('J') != -1) {
                var maxType = replacements.chars()
                    .mapToObj(r -> (char) r)
                    .map(r -> cards.replace('J', r))
                    .map(Hand::getType)
                    .max(Comparator.comparingInt(a -> a))
                    .orElseThrow();

                cardsValue = maxType.toString() + value;
                return;
            }

            cardsValue = getType(cards) + value.toString();
        }

        public static int compare(Hand hand1, Hand hand2) {
            return hand1.cardsValue.compareTo(hand2.cardsValue);
        }

        public static int getType(String cards) {
            return Integer.parseInt(
                StringUtil.tally(cards).values().stream()
                    .sorted(Comparator.reverseOrder())
                    .map(String::valueOf)
                    .collect(Collectors.joining())
            );
        }
    }
}

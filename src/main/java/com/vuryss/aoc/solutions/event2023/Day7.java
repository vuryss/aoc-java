package com.vuryss.aoc.solutions.event2023;

import com.google.common.base.Joiner;
import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.*;

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
        final static List<Character> weights1 = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
        final static List<Character> weights2 = List.of('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');
        static List<Character> replacements = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');

        public Hand(String cards, boolean adjustForPart2) {
            var value = new StringBuilder();

            for (var card: cards.toCharArray()) {
                value.append(Integer.toString(adjustForPart2 ? weights2.indexOf(card) : weights1.indexOf(card), 16));
            }

            if (adjustForPart2 && cards.indexOf('J') != -1) {
                var max = replacements.stream()
                    .map(r -> cards.replace('J', r))
                    .map(Hand::getType)
                    .max(Comparator.comparingInt(a -> a))
                    .get();

                cardsValue = max.toString() + value;
                return;
            }

            cardsValue = getType(cards) + value.toString();
        }

        public static int compare(Hand hand1, Hand hand2) {
            return hand1.cardsValue.compareTo(hand2.cardsValue);
        }

        public static int getType(String cards) {
            var cardCountsValues = new ArrayList<>(StringUtil.tally(cards).values());
            cardCountsValues.sort(Comparator.reverseOrder());
            return typeValue.get(Joiner.on("").join(cardCountsValues));
        }
    }
}

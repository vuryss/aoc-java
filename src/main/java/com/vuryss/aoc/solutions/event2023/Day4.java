package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.IntUtil;
import com.vuryss.aoc.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day4 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """,
            "13"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """,
            "30"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var sum = 0;

        for (var line: lines) {
            var parts = line.split(": ");
            var numberList = parts[1].split(" \\| ");
            var winningNumbers = StringUtil.ints(numberList[0]);
            var myNumbers = StringUtil.ints(numberList[1]);
            var matchedNumbersCount = myNumbers.stream().filter(winningNumbers::contains).count();

            if (matchedNumbersCount > 0) {
                sum += (int) Math.pow(2, matchedNumbersCount - 1);
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var sum = 0;
        var numberOfCards = new HashMap<Integer, Integer>();

        for (var line: lines) {
            var parts = line.split(": ");
            int cardNumber = IntUtil.parseUnsignedInteger(parts[0]);
            numberOfCards.put(cardNumber, numberOfCards.getOrDefault(cardNumber, 0) + 1);

            var numberList = parts[1].split(" \\| ");
            var winningNumbers = StringUtil.ints(numberList[0]);
            var myNumbers = StringUtil.ints(numberList[1]);
            var matchedNumbersCount = myNumbers.stream().filter(winningNumbers::contains).count();
            var numberOfWonCards = numberOfCards.getOrDefault(cardNumber, 1);

            for (var cardOffset = 1; cardOffset <= matchedNumbersCount; cardOffset++) {
                numberOfCards.put(
                    cardNumber + cardOffset,
                    numberOfCards.getOrDefault(cardNumber + cardOffset, 0) + numberOfWonCards
                );
            }

            sum += numberOfCards.get(cardNumber);
        }

        return String.valueOf(sum);
    }
}

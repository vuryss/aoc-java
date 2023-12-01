package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.*;

public class Day1 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """,
            "142"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """,
            "281"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        long sum = 0L;

        for (var line: lines) {
            var lineNumberPositions = new TreeMap<Integer, Integer>();

            for (var i = 0; i < line.length(); i++) {
                var c = line.charAt(i);

                if (Character.isDigit(c)) {
                    lineNumberPositions.put(i, Character.getNumericValue(c));
                }
            }

            sum += lineNumberPositions.firstEntry().getValue() * 10 + lineNumberPositions.lastEntry().getValue();
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var digitWords = Map.of(
            1, "one",
            2, "two",
            3, "three",
            4, "four",
            5, "five",
            6, "six",
            7, "seven",
            8, "eight",
            9, "nine"
        );

        var lines = input.trim().split("\n");
        long sum = 0L;

        for (var line: lines) {
            var lineNumberPositions = new TreeMap<Integer, Integer>();

            for (var digitWord: digitWords.entrySet()) {
                var digitPosition = line.indexOf(digitWord.getValue());
                var lastDigitPosition = line.lastIndexOf(digitWord.getValue());

                if (digitPosition != -1) {
                    lineNumberPositions.put(digitPosition, digitWord.getKey());
                }

                if (lastDigitPosition != -1) {
                    lineNumberPositions.put(lastDigitPosition, digitWord.getKey());
                }
            }

            for (var i = 0; i < line.length(); i++) {
                var c = line.charAt(i);

                if (Character.isDigit(c)) {
                    lineNumberPositions.put(i, Character.getNumericValue(c));
                }
            }

            sum += lineNumberPositions.firstEntry().getValue() * 10 + lineNumberPositions.lastEntry().getValue();
        }

        return String.valueOf(sum);
    }
}

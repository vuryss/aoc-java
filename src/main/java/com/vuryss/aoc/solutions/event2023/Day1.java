package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.*;
import java.util.stream.Stream;

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
        int sum = 0;

        for (var line: lines) {
            var numbers = line.chars().filter(Character::isDigit).map(Character::getNumericValue).toArray();
            sum += numbers[0] * 10 + numbers[numbers.length - 1];
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var digitWords = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        var lines = input.trim().split("\n");
        long sum = 0L;

        for (var line: lines) {
            var numbers = new LinkedList<Integer>();

            for (int i = 0; i < line.length(); i++) {
                int index = i;
                Stream.of(line.charAt(i)).filter(Character::isDigit).forEach(c -> numbers.add(Character.getNumericValue(c)));
                digitWords.stream().filter(e -> line.startsWith(e, index)).forEach(e -> numbers.add(digitWords.indexOf(e) + 1));
            }

            sum += numbers.getFirst() * 10 + numbers.getLast();
        }

        return String.valueOf(sum);
    }
}

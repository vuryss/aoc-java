package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day20 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1
            2
            -3
            3
            -2
            0
            4
            """,
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1
            2
            -3
            3
            -2
            0
            4
            """,
            "1623178306"
        );
    }

    @Override
    public String part1Solution(String input) {
        return decode(input, 1, 1);
    }

    @Override
    public String part2Solution(String input) {
        return decode(input, 10, 811589153);
    }

    private String decode(String input, int times, int decryptionKey) {
        var numbers = new ArrayList<Number>();
        Number zero = new Number(0);

        for (var line: input.trim().split("\n")) {
            var number = new Number(Long.parseLong(line) * decryptionKey);
            numbers.add(number);

            if (number.value == 0) {
                zero = number;
            };
        }

        var decrypted = new ArrayList<>(numbers);
        var modNumber = numbers.size() - 1;

        for (var i = 0; i < times; i++) {
            for (var number: numbers) {
                var currentPosition = decrypted.indexOf(number);
                var newPosition = currentPosition + number.value;

                if (newPosition > 0) {
                    newPosition = (newPosition % modNumber);
                } else if (newPosition < 0) {
                    newPosition = numbers.size() + (newPosition % modNumber) - 1;
                }

                decrypted.remove(number);
                decrypted.add((int) newPosition, number);
            }
        }

        var zeroIndex = decrypted.indexOf(zero);

        var i1 = (zeroIndex + 1000) % numbers.size();
        var i2 = (zeroIndex + 2000) % numbers.size();
        var i3 = (zeroIndex + 3000) % numbers.size();

        return String.valueOf(decrypted.get(i1).value + decrypted.get(i2).value + decrypted.get(i3).value);
    }

    static class Number {
        public long value;

        Number(long value) {
            this.value = value;
        }
    }
}

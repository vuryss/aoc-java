package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    private final static long FACTOR_A = 16807;
    private final static long FACTOR_B = 48271;
    private final static long MODULUS = 2147483647;
    private final static int MASK = 0xFFFF;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Generator A starts with 65
            Generator B starts with 8921
            """,
            "588"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Generator A starts with 65
            Generator B starts with 8921
            """,
            "309"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var numbers = StringUtil.longs(input);
        var a = numbers.get(0);
        var b = numbers.get(1);
        var count = 0;

        for (long i = 0L; i < 40_000_000L; i++) {
            a = (a * FACTOR_A) % MODULUS;
            b = (b * FACTOR_B) % MODULUS;

            if ((a & MASK) == (b & MASK)) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var numbers = StringUtil.longs(input);
        var a = new GenA(numbers.get(0));
        var b = new GenB(numbers.get(1));
        var count = 0;

        for (long i = 0L; i < 5_000_000L; i++) {
            if ((a.next() & MASK) == (b.next() & MASK)) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    private static class GenA {
        private long value;

        public GenA(long value) {
            this.value = value;
        }

        public long next() {
            do {
                value = (value * FACTOR_A) % MODULUS;
            } while ((value & 3) != 0); // x % 2^k == x & (2^k - 1)

            return value;
        }
    }

    private static class GenB {
        private long value;

        public GenB(long value) {
            this.value = value;
        }

        public long next() {
            do {
                value = (value * FACTOR_B) % MODULUS;
            } while ((value & 7) != 0); // // x % 2^k == x & (2^k - 1)

            return value;
        }
    }
}

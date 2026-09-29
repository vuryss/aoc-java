package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    public static final int SUBJECT_NUMBER = 7;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            5764801
            17807724
            """,
            "14897079"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var ints = StringUtil.ints(input);
        int cardPublicKey = ints.get(0);
        int doorPublicKey = ints.get(1);

        int cardLoopSize = reverseEngineerLoopSize(cardPublicKey);
        int doorLoopSize = reverseEngineerLoopSize(doorPublicKey);

        long encryptionKey = transform(doorPublicKey, cardLoopSize);

        return encryptionKey + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    private int reverseEngineerLoopSize(int result) {
        long value = 1;
        int loopSize = 0;

        do {
            value *= SUBJECT_NUMBER;
            value %= 20201227;
            loopSize++;
        } while (value != result);

        return loopSize;
    }

    private long transform(int subjectNumber, int loopSize) {
        long value = 1;

        for (int i = 0; i < loopSize; i++) {
            value *= subjectNumber;
            value %= 20201227;
        }

        return value;
    }
}

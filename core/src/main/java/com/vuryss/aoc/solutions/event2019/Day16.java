package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    final int[] pattern = new int[]{0, 1, 0, -1};

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "80871224585914546619083218645595", "24176176",
            "19617804207202209144916044189917", "73745418",
            "69317163492948606335995924319873", "52432133"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "03036732577212944063491565474664", "84462026",
            "02935109699940807407585447034323", "78725270",
            "03081770884921959731165446850517", "53553731"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var data = StringUtil.toByteArray(input.trim());

        for (var i = 0; i < 100; i++) {
            var nextData = new byte[data.length];

            for (var j = 0; j < data.length; j++) {
                int acc = 0;
                for (var k = 0; k < data.length; k++) {
                    var patternPosition = ((k + 1) / (j + 1)) % pattern.length;
                    acc += data[k] * pattern[patternPosition];
                }
                nextData[j] = (byte) Math.abs(acc % 10);
            }

            data = nextData;
        }

        var finalData = data;
        return IntStream.range(0, 8).mapToObj(i -> String.valueOf(finalData[i])).collect(Collectors.joining());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        var neededSize = input.length() * 10000 - Integer.parseInt(input.substring(0, 7));
        byte[] data = new byte[neededSize];
        long acc;
        char[] reversed = (new StringBuilder(input)).reverse().toString().toCharArray();

        for (var i = 0; i < neededSize; i++) {
            data[i] = (byte) (reversed[i % reversed.length] - '0');
        }

        for (var i = 0; i < 100; i++) {
            acc = 0;

            for (var j = 0; j < data.length; j++) {
                acc += data[j];
                data[j] = (byte) (acc % 10);
            }
        }

        return IntStream.range(0, 8)
            .mapToObj(i -> Byte.toString(data[data.length - 1 - i]))
            .collect(Collectors.joining());
    }
}

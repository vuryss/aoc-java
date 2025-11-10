package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("10000", "01100");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var length = isTest ? 20 : 272;
        var disk = fillDiskWithDragonCurve(input, length);

        return checksum(disk);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return checksum(fillDiskWithDragonCurve(input, 35651584));
    }

    private String fillDiskWithDragonCurve(String input, int diskSize) {
        input = input.trim();

        while (input.length() < diskSize) {
            input = dragonCurve(input);
        }

        return input.substring(0, diskSize);
    }

    private String dragonCurve(String input) {
        var b = new StringBuilder(input).reverse().toString().chars()
            .mapToObj(c -> c == '0' ? "1" : "0")
            .collect(Collectors.joining());

        return input + "0" + b;
    }

    private String checksum(String input) {
        while (input.length() % 2 == 0) {
            input = checksumPass(input);
        }

        return input;
    }

    private String checksumPass(String input) {
        return StringUtil.chunk(input, 2).stream()
            .map(chunk -> chunk.charAt(0) == chunk.charAt(1) ? "1" : "0")
            .collect(Collectors.joining());
    }
}

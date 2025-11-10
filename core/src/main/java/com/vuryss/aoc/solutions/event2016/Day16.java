package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;

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

    /**
     * The number of checksum passes would be the number of times we can divide the dragon curve output by 2.
     * For 272, we can divide it by 2 four times (272 -> 136 -> 68 -> 34 -> 17)
     * Each number of the final checksum would be calculated from a chunk of the dragon curve that is 2^{num-of-passes} long
     * The chunk size can be calculated by diskSize & ~(diskSize - 1)
     *
     * Explanation:
     *   272 is 100010000
     *   271 is 100001111
     *  ~271 is 011110000
     *  so
     *  272 & ~271 is 000010000 which is 16 total characters for a single checksum character 2^{4 passes}
     *
     *  First 16 chars would result in the first checksum char, second 16 chars would result in the second checksum char, etc.
     *
     *  Second logic would be:
     *  - If there are an odd number of '1' bits in the chunk, the final checksum character would be '0'
     *  - If there are an even number of '1' bits in the chunk, the final checksum character would be '1'
     */
    private String checksum(String input) {
        var inputLength = input.length();
        var chunkSize = inputLength & -inputLength;
        var checksum = new StringBuilder(inputLength / chunkSize);

        for (var i = 0; i < inputLength; i += chunkSize) {
            var chunk = input.substring(i, i + chunkSize);

            checksum.append(chunk.chars().filter(c -> c == '1').count() % 2 == 0 ? "1" : "0");
        }

        return checksum.toString();
    }

    // Brute force solution
//    private String checksumBruteForce(String input) {
//        while (input.length() % 2 == 0) {
//            input = checksumPass(input);
//        }
//
//        return input;
//    }
//
//    private String checksumPass(String input) {
//        return StringUtil.chunk(input, 2).stream()
//            .map(chunk -> chunk.charAt(0) == chunk.charAt(1) ? "1" : "0")
//            .collect(Collectors.joining());
//    }
}

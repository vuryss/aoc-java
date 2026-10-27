package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """,
            "198"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """,
            "230"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var gamma = new StringBuilder();

        for (var i = 0; i < lines[0].length(); i++) {
            var ones = 0;

            for (var line: lines) {
                if (line.charAt(i) == '1') {
                    ones++;
                }
            }

            gamma.append(ones > lines.length / 2 ? '1' : '0');
        }

        var gammaRate = Integer.parseInt(gamma.toString(), 2);
        var epsilonRate = ~gammaRate & ((int) Math.pow(2, lines[0].length()) - 1);

        return String.valueOf(gammaRate * epsilonRate);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var oxygenList = List.of(lines);
        var co2List = List.of(lines);
        var numberSize = lines[0].length();

        for (var i = 0; i < numberSize && oxygenList.size() > 1; i++) {
            var charIndex = i;
            var ones = oxygenList.stream().filter(item -> item.charAt(charIndex) == '1').count();
            var charToKeep = ones * 2 < oxygenList.size() ? '0' : '1';
            oxygenList = oxygenList.stream().filter(item -> item.charAt(charIndex) == charToKeep).toList();
        }

        for (var i = 0; i < numberSize && co2List.size() > 1; i++) {
            var charIndex = i;
            var ones = co2List.stream().filter(item -> item.charAt(charIndex) == '1').count();
            var charToKeep = ones * 2 < co2List.size() ? '1' : '0';
            co2List = co2List.stream().filter(item -> item.charAt(charIndex) == charToKeep).toList();
        }

        var oxygenRating = Integer.parseInt(oxygenList.getFirst(), 2);
        var co2Rating = Integer.parseInt(co2List.getFirst(), 2);

        return String.valueOf(oxygenRating * co2Rating);
    }
}

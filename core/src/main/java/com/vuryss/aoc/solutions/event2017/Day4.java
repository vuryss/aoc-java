package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            aa bb cc dd ee
            aa bb cc dd aa
            aa bb cc dd aaa
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            abcde fghij
            abcde xyz ecdab
            a ab abc abd abf abj
            iiii oiii ooii oooi oooo
            oiii ioii iioi iiio
            """,
            "3"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var count = 0;
        var wordSet = new HashSet<String>();

        for (var line: lines) {
            var words = line.split(" ");
            wordSet.clear();
            wordSet.addAll(Arrays.asList(words));

            if (wordSet.size() == words.length) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var count = 0;
        var wordSet = new HashSet<String>();

        lineLoop:
        for (var line: lines) {
            var words = line.split(" ");
            wordSet.clear();

            for (var word: words) {
                var chars = word.toCharArray();
                Arrays.sort(chars);

                if (!wordSet.add(new String(chars))) {
                    continue  lineLoop;
                }
            }

            count++;
        }

        return String.valueOf(count);
    }
}

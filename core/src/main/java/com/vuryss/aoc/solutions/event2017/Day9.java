package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "{}",
            "1",
            "{{{}}}",
            "6",
            "{{},{}}",
            "5",
            "{{{},{},{{}}}}",
            "16",
            "{<a>,<a>,<a>,<a>}",
            "1",
            "{{<ab>},{<ab>},{<ab>},{<ab>}}",
            "9",
            "{{<!!>},{<!!>},{<!!>},{<!!>}}",
            "9",
            "{{<a!>},{<a!>},{<a!>},{<ab>}}",
            "3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "<>",
            "0",
            "<random characters>",
            "17",
            "<<<<>",
            "3",
            "<{!>}>",
            "2",
            "<!!>",
            "0",
            "<!!!>>",
            "0",
            "<{o\"i!a,<{i<a>",
            "10"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var chars = input.trim().toCharArray();
        var score = 0;
        var groupLevel = 1;
        var inGarbage = false;
        var ignoreNext = false;

        for (var c: chars) {
            if (ignoreNext) {
                ignoreNext = false;
                continue;
            }

            if (inGarbage) {
                if (c == '!') {
                    ignoreNext = true;
                } else if (c == '>') {
                    inGarbage = false;
                }

                continue;
            }

            if (c == '{') {
                score += groupLevel++;
            } else if (c == '}') {
                groupLevel--;
            } else if (c == '<') {
                inGarbage = true;
            }
        }

        return String.valueOf(score);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var chars = input.trim().toCharArray();
        var count = 0;
        var inGarbage = false;
        var ignoreNext = false;

        for (var c: chars) {
            if (ignoreNext) {
                ignoreNext = false;
                continue;
            }

            if (inGarbage) {
                if (c == '!') {
                    ignoreNext = true;
                } else if (c == '>') {
                    inGarbage = false;
                } else {
                    count++;
                }

                continue;
            }

            if (c == '<') {
                inGarbage = true;
            }
        }

        return String.valueOf(count);
    }
}

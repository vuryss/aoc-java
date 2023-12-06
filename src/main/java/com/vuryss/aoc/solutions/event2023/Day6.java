package com.vuryss.aoc.solutions.event2023;

import com.google.common.base.Joiner;
import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.ListUtil;

import java.util.*;

@SuppressWarnings("unused")
public class Day6 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Time:      7  15   30
            Distance:  9  40  200
            """,
            "288"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Time:      7  15   30
            Distance:  9  40  200
            """,
            "71503"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        long product = 1L;

        var times = ListUtil.extractUnsignedIntegers(lines[0]);
        var records = ListUtil.extractUnsignedIntegers(lines[1]);

        for (var i = 0; i < times.size(); i++) {
            var raceTime = times.get(i);
            var waysToWinCount = 0;

            for (var time = 1; time < raceTime; time++) {
                var distance = (raceTime - time) * time;

                if (distance > records.get(i)) {
                    waysToWinCount++;
                }
            }

            product *= waysToWinCount;
        }

        return String.valueOf(product);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var times = ListUtil.extractUnsignedIntegers(lines[0]);
        var records = ListUtil.extractUnsignedIntegers(lines[1]);
        var totalTime = Long.parseLong(Joiner.on("").join(times));
        var totalRecord = Long.parseLong(Joiner.on("").join(records));
        var waysToWinCount = 0;

        for (var currentTime = 1L; currentTime < totalTime; currentTime++) {
            var distance = (totalTime - currentTime) * currentTime;

            if (distance > totalRecord) {
                waysToWinCount++;
            } else if (distance < totalRecord && waysToWinCount > 0) {
                break;
            }
        }

        return String.valueOf(waysToWinCount);
    }
}

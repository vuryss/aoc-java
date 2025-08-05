package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day7 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            16,1,2,0,4,2,7,1,2,14
            """,
            "37"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            16,1,2,0,4,2,7,1,2,14
            """,
            "168"
        );
    }

    @Override
    public String part1Solution(String input) {
        var positions = StringUtil.ints(input);
        var sum = positions.stream().mapToInt(i -> i).sum();
        var avg = sum / positions.size();
        var min = positions.stream().mapToInt(p -> Math.abs(p - avg)).sum();

        var i = 0;

        while (i++ >= 0) {
            var pos1 = avg - i;
            var fuel1 = positions.stream().mapToInt(p -> Math.abs(p - pos1)).sum();
            var pos2 = avg + i;
            var fuel2 = positions.stream().mapToInt(p -> Math.abs(p - pos2)).sum();

            if (fuel1 < min) {
                min = fuel1;
            } else if (fuel2 < min) {
                min = fuel2;
            } else {
                break;
            }
        }

        return String.valueOf(min);
    }

    @Override
    public String part2Solution(String input) {
        var positions = StringUtil.ints(input);
        var sum = positions.stream().mapToInt(i -> i).sum();
        var avg = sum / positions.size();
        var min = positions.stream().mapToInt(p -> Math.abs(p - avg)).map(p -> p * (p + 1) / 2).sum();

        var i = 0;

        while (i++ >= 0) {
            var pos1 = avg - i;
            var fuel1 = positions.stream().mapToInt(p -> Math.abs(p - pos1)).map(p -> p * (p + 1) / 2).sum();
            var pos2 = avg + i;
            var fuel2 = positions.stream().mapToInt(p -> Math.abs(p - pos2)).map(p -> p * (p + 1) / 2).sum();

            if (fuel1 < min) {
                min = fuel1;
            } else if (fuel2 < min) {
                min = fuel2;
            } else {
                break;
            }
        }

        return String.valueOf(min);
    }
}

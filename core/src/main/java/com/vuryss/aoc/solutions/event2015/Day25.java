package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var numbers = StringUtil.ints(input);
        var targetRow = numbers.get(0);
        var targetColumn = numbers.get(1);
        var currentValue = 20151125L;
        var maxRow = 2;

        while (true) {
            for (var column = 1; column <= maxRow; column++) {
                currentValue *= 252533;
                currentValue %= 33554393;

                if ((maxRow - column + 1) == targetRow && column == targetColumn) {
                    return String.valueOf(currentValue);
                }
            }

            maxRow++;
        }
    }

    @Override
    public String part2Solution(String input) {
        return "Merry Christmas!";
    }
}

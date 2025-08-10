package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.*;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "ADVENT", "6",
            "A(1x5)BC", "7",
            "(3x3)XYZ", "9",
            "A(2x2)BCD(2x2)EFG", "11",
            "(6x1)(1x3)A", "6",
            "X(8x2)(3x3)ABCY", "18"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "(3x3)XYZ", "9",
            "X(8x2)(3x3)ABCY", "20",
            "(27x12)(20x12)(13x14)(7x10)(1x12)A", "241920",
            "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", "445"
        );
    }

    @Override
    public String part1Solution(String input) {
        return String.valueOf(calculateDecompressedLength(input.trim(), false));
    }

    @Override
    public String part2Solution(String input) {
        return String.valueOf(calculateDecompressedLength(input.trim(), true));
    }

    private long calculateDecompressedLength(String input, boolean versionTwo) {
        var index = 0;
        long length = 0;

        while (index < input.length()) {
            if (input.charAt(index) == '(') {
                var marker = Regex.match("^\\((\\d+)x(\\d+)\\)", input.substring(index));
                var numbers = StringUtil.ints(marker);

                index += Objects.requireNonNull(marker).length();

                var subLength = versionTwo
                    ? calculateDecompressedLength(input.substring(index, index + numbers.get(0)), true)
                    : numbers.get(0);

                length += subLength * numbers.get(1);
                index += numbers.get(0);

                continue;
            }

            index++;
            length++;
        }

        return length;
    }
}

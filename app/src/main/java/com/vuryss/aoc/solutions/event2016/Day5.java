package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "abc", "18f47a30"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "abc", "05ace8e3"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        input = input.trim();
        var index = 0;
        var doorId = new StringBuilder();

        while (doorId.length() < 8) {
            var hash = DigestUtils.md5Hex(input + index++);

            if (hash.startsWith("00000")) {
                doorId.append(hash.charAt(5));
            }
        }

        return doorId.toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        var index = 0;
        var chars = new char[8];
        var usedPositions = new HashSet<Integer>();

        while (usedPositions.size() < 8) {
            var hash = DigestUtils.md5Hex(input + index++);

            if (hash.startsWith("00000")) {
                var position = Character.getNumericValue(hash.charAt(5));

                if (position >= 0 && position < 8 && !usedPositions.contains(position)) {
                    chars[position] = hash.charAt(6);
                    usedPositions.add(position);
                }
            }
        }

        return new String(chars);
    }
}

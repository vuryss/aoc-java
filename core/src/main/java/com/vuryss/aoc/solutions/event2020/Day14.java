package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    private static final Pattern pattern = Pattern.compile("(\\d+).+?(\\d+)");
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
            mem[8] = 11
            mem[7] = 101
            mem[8] = 0
            """,
            "165"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
            """,
            "208"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        // replace all X with 0, do mask | number -> all 1s would be put on the number
        // replace all X with 1, do mask & number -> all 0s would be put on the number
        long andMask = 0, orMask = 0;
        HashMap<Long, Long> mem = new HashMap<>();

        for (var line: input.trim().split("\n")) {
            if (line.startsWith("mask = ")) {
                line = line.substring(7);
                andMask = Long.parseLong(line.replace('X', '1'), 2);
                orMask = Long.parseLong(line.replace('X', '0'), 2);
                continue;
            }
            var matcher = pattern.matcher(line);
            matcher.find();
            long address = Long.parseLong(matcher.group(1));
            long value = Long.parseLong(matcher.group(2));

            mem.put(address, (orMask | value) & andMask);
        }

        return mem.values().stream().mapToLong(l -> l).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        HashMap<Long, Long> mem = new HashMap<>();
        String mask = "";
        long orMask = 0;

        for (var line: input.trim().split("\n")) {
            if (line.startsWith("mask = ")) {
                mask = line.substring(7);
                orMask = Long.parseLong(mask.replace('X', '0'), 2);
                continue;
            }

            var matcher = pattern.matcher(line);
            matcher.find();
            long address = Long.parseLong(matcher.group(1)) | orMask;
            long value = Long.parseLong(matcher.group(2));

            mapAddresses(mask, 0, mem, address, value);
        }

        return mem.values().stream().mapToLong(l -> l).sum() + "";
    }

    private void mapAddresses(String mask, int index, Map<Long, Long> mem, long address, long value) {
        var pos = mask.indexOf('X', index);

        // Base case
        if (pos == -1) {
            mem.put(address, value);
            return;
        }

        long bit = 1L << (35 - pos);
        mapAddresses(mask, pos + 1, mem, address | bit, value);
        mapAddresses(mask, pos + 1, mem, address & ~bit, value);
    }
}

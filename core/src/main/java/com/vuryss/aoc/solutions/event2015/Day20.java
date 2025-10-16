package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var target = Integer.parseInt(input.trim());
        var maxElf = target / 10;
        var houses = new int[maxElf + 1];

        for (var elf = 1; elf <= maxElf; elf++) {
            for (var house = elf; house <= maxElf; house += elf) {
                houses[house] += elf;
            }
        }

        for (var i = 1; i < houses.length; i++) {
            if (houses[i] >= maxElf) {
                return String.valueOf(i);
            }
        }

        return "-not found-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var target = Integer.parseInt(input.trim());
        var maxElf = target / 11;
        var houses = new int[maxElf + 1];

        for (var elf = 1; elf <= maxElf; elf++) {
            for (var house = elf; house <= maxElf && house <= elf * 50; house += elf) {
                houses[house] += elf;
            }
        }

        for (var i = 1; i < houses.length; i++) {
            if (houses[i] >= maxElf) {
                return String.valueOf(i);
            }
        }

        return "-not found-";
    }
}

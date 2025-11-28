package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
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
        int[][] fabric = new int[1000][1000];
        var claims = parseClaims(input);

        for (var claim: claims) {
            for (var x = claim[1]; x < claim[1] + claim[3]; x++) {
                for (var y = claim[2]; y < claim[2] + claim[4]; y++) {
                    fabric[x][y]++;
                }
            }
        }

        return String.valueOf(
            Arrays.stream(fabric)
                .flatMapToInt(Arrays::stream)
                .filter(i -> i > 1)
                .count()
        );
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        int[][] fabric = new int[1000][1000];
        var claims = parseClaims(input);

        for (var claim: claims) {
            for (var x = claim[1]; x < claim[1] + claim[3]; x++) {
                for (var y = claim[2]; y < claim[2] + claim[4]; y++) {
                    fabric[x][y]++;
                }
            }
        }

        outer:
        for (var claim: claims) {
            for (var x = claim[1]; x < claim[1] + claim[3]; x++) {
                for (var y = claim[2]; y < claim[2] + claim[4]; y++) {
                    if (fabric[x][y] > 1) {
                        continue outer;
                    }
                }
            }

            return String.valueOf(claim[0]);
        }

        return "-not found-";
    }

    private int[][] parseClaims(String input) {
        var lines = input.trim().split("\n");
        int[][] claims = new int[lines.length][5];

        for (var i = 0; i < lines.length; i++) {
            claims[i] = StringUtil.ints(lines[i]).stream().mapToInt(Integer::intValue).toArray();
        }

        return claims;
    }
}

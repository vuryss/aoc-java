package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    private final HashMap<Integer, String> hashCache = new HashMap<>();

    @Override
    public Map<String, String> part1Tests() {
        return Map.of("abc", "22728");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("abc", "22551");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, true);
    }

    private String solve(String input, boolean stretch) {
        var salt = input.trim();
        var index = 0;
        var keys = new ArrayList<Integer>();
        hashCache.clear();

        while (keys.size() < 64) {
            var hash = hash(salt, index, stretch);
            var triplet = Regex.match("(\\w)\\1\\1", hash);

            if (triplet != null) {
                var fiveOfAKind = triplet.repeat(2).substring(0, 5);

                for (var i = index + 1; i <= index + 1000; i++) {
                    if (hash(salt, i, stretch).contains(fiveOfAKind)) {
                        keys.add(index);
                        break;
                    }
                }
            }

            index++;
        }

        return String.valueOf(keys.get(63));
    }

    private String hash(String salt, int index, boolean stretch) {
        if (!hashCache.containsKey(index)) {
            var hash = StringUtil.md5(salt + index);

            if (stretch) {
                for (int i = 0; i < 2016; i++) {
                    hash = StringUtil.md5(hash);
                }
            }

            hashCache.put(index, hash);
        }

        return hashCache.get(index);
    }
}

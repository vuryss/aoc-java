package com.vuryss.aoc.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class StringUtil {
    public static Map<Character, Integer> tally(String string) {
        var characterCount = new HashMap<Character, Integer>();

        for (var c: string.toCharArray()) {
            characterCount.put(c, characterCount.getOrDefault(c, 0) + 1);
        }

        return characterCount;
    }

    public static Set<Integer> uniqueInts(String string) {
        return new HashSet<>(ints(string));
    }

    public static List<Integer> ints(String s) {
        return Regex.matchAll("\\d+", s).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<Long> longs(String s) {
        return Regex.matchAll("\\d+", s).stream().map(Long::parseLong).toList();
    }

    public static List<Long> slongs(String s) {
        return Regex.matchAll("\\-?\\d+", s).stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    public static String md5(String string) {
        return Hashing.md5().hashString(string, StandardCharsets.UTF_8).toString();
    }
}

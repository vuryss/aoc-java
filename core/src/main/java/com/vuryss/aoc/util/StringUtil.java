package com.vuryss.aoc.util;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class StringUtil {
    private static final String[] HEX_TO_BIN = {
        "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
        "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"
    };

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
        return Regex.matchAll("\\d+", s).stream().map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Integer> sints(String s) {
        return Regex.matchAll("\\-?\\d+", s).stream().map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Long> longs(String s) {
        return Regex.matchAll("\\d+", s).stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public static List<Long> slongs(String s) {
        return Regex.matchAll("\\-?\\d+", s).stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    public static String md5(String string) {
        return Hashing.md5().hashString(string, StandardCharsets.UTF_8).toString();
    }

    public static List<String> chunk(String string, int size) {
        int len = string.length();
        int full = len / size;
        List<String> out = new ArrayList<>(full);

        for (int i = 0; i < len; i += size) {
            out.add(string.substring(i, i + size));
        }

        return out;
    }

    public static String hex2bin(String string) {
        var bin = new StringBuilder();

        for (var c: string.toCharArray()) {
            bin.append(HEX_TO_BIN[Character.digit(c, 16)]);
        }

        return bin.toString();
    }

    public static LinkedList<Character> toCharacterQueue(String input) {
        var list = new LinkedList<Character>();

        for (var c: input.toCharArray()) {
            list.add(c);
        }

        return list;
    }
}

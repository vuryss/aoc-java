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

    public static List<Integer> sints(String s) {
        return Regex.matchAll("\\-?\\d+", s).stream().map(Integer::parseInt).collect(Collectors.toList());
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

    public static String hex2bin(String string) {
        var map = new HashMap<Character, String>(){{
            put('0', "0000");
            put('1', "0001");
            put('2', "0010");
            put('3', "0011");
            put('4', "0100");
            put('5', "0101");
            put('6', "0110");
            put('7', "0111");
            put('8', "1000");
            put('9', "1001");
            put('A', "1010");
            put('B', "1011");
            put('C', "1100");
            put('D', "1101");
            put('E', "1110");
            put('F', "1111");
        }};

        return string.trim().chars().mapToObj(c -> map.get((char) c)).collect(Collectors.joining());
    }
}

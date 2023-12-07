package com.vuryss.aoc.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringUtil {
    public static Map<Character, Integer> tally(String string) {
        var characterCount = new HashMap<Character, Integer>();

        for (var c: string.toCharArray()) {
            characterCount.put(c, characterCount.getOrDefault(c, 0) + 1);
        }

        return characterCount;
    }

    public static Set<Integer> uniqueIntegers(String string) {
        var set = new HashSet<Integer>();
        var matches = Regex.matchAll("\\d+", string);

        for (var match: matches) {
            set.add(Integer.parseInt(match));
        }

        return set;
    }
}

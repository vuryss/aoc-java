package com.vuryss.aoc.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Regex {
    public static String match(String regex, String input) {
        var m = Pattern.compile(regex).matcher(input);

        return m.find() ? m.group() : null;
    }

    public static List<String> matchAll(String regex, String input) {
        var m = Pattern.compile(regex).matcher(input);
        var list = new ArrayList<String>();

        while (m.find()) {
            list.add(m.group());
        }

        return list;
    }

    public static boolean matches(String regex, String input) {
        return Pattern.compile(regex).matcher(input).find();
    }

    public static List<String> matchGroups(String regex, String input) {
        var m = Pattern.compile(regex).matcher(input);
        var l = new ArrayList<String>();

        if (!m.find()) {
            return null;
        }

        for (var i = 1; i <= m.groupCount(); i++) {
            var val = m.group(i);

            if (val != null) {
                l.add(val);
            }
        }

        return l;
    }

    @NotNull
    public static HashMap<String, String> matchNamedGroups(String regex, String input) {
        var m = Pattern.compile(regex).matcher(input);
        var map = new HashMap<String, String>();

        if (!m.find()) {
            throw new RuntimeException("No matches found");
        }

        for (var es: m.namedGroups().entrySet()) {
            map.put(es.getKey(), m.group(es.getKey()));
        }

        return map;
    }
}

package com.vuryss.aoc.util;

import java.util.ArrayList;
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
            l.add(m.group(i));
        }

        return l;
    }
}

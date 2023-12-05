package com.vuryss.aoc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListUtil {
    public static List<Integer> extractUnsignedIntegers(String s) {
        var list = new ArrayList<Integer>();

        var p = Pattern.compile("\\d+");
        var m = p.matcher(s);

        while (m.find()) {
            list.add(Integer.parseInt(m.group()));
        }

        return list;
    }

    public static List<Integer> extractSignedIntegers(String s, boolean allowNegative) {
        var list = new ArrayList<Integer>();

        var p = Pattern.compile("-?\\d+");
        var m = p.matcher(s);

        while (m.find()) {
            list.add(Integer.parseInt(m.group()));
        }

        return list;
    }

    public static List<Long> extractUnsignedLongs(String s) {
        var list = new ArrayList<Long>();

        var p = Pattern.compile("\\d+");
        var m = p.matcher(s);

        while (m.find()) {
            list.add(Long.parseLong(m.group()));
        }

        return list;
    }
}

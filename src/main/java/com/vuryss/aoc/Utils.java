package com.vuryss.aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public class Utils {
    public static List<Integer> toIntList(String[] strings) {
        var list = new ArrayList<Integer>();

        for (var str: strings) {
            list.add(Integer.parseInt(str));
        }

        return list;
    }

    public static List<Long> toLongList(String[] strings) {
        var list = new ArrayList<Long>();

        for (var str: strings) {
            list.add(Long.parseLong(str));
        }

        return list;
    }

    public static LinkedList<Character> toCharacterQueue(String input) {
        var list = new LinkedList<Character>();

        for (var c: input.toCharArray()) {
            list.add(c);
        }

        return list;
    }

    public static int getNumberFromString(String s) {
        return Integer.parseInt(s.replaceAll("[^\\d-]+", ""));
    }

    public static Integer[] extractIntegersFromString(String s) {
        var list = new LinkedList<Integer>();

        var p = Pattern.compile("-?\\d+");
        var m = p.matcher(s);

        while (m.find()) {
            list.add(Integer.parseInt(m.group()));
        }

        return list.toArray(new Integer[0]);
    }

    public static int manhattan(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}

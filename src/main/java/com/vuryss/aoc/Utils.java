package com.vuryss.aoc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public static Queue<Character> toCharacterQueue(String input) {
        var list = new LinkedList<Character>();

        for (var c: input.toCharArray()) {
            list.add(c);
        }

        return list;
    }
}

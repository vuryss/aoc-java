package com.vuryss.aoc.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
    public static List<Integer> sort(List<Integer> list) {
        var sorted = new ArrayList<>(list);

        sorted.sort(Integer::compareTo);

        return sorted;
    }
}

package com.vuryss.aoc.util;

import java.util.ArrayList;
import java.util.List;

public class Combinatorics {
    public static <T> List<List<T>> combinations(List<T> a, int k) {
        int n = a.size();
        if (k <= 0 || k > n) return List.of();

        int[] idx = new int[k];
        for (int i = 0; i < k; i++) idx[i] = i;

        List<List<T>> res = new ArrayList<>();

        while (true) {
            var combo = new ArrayList<T>(k);
            for (int i = 0; i < k; i++) combo.add(a.get(idx[i]));
            res.add(combo);

            int i = k - 1;
            while (i >= 0 && idx[i] == n - k + i) i--;
            if (i < 0) break;
            idx[i]++;
            for (int j = i + 1; j < k; j++) idx[j] = idx[j - 1] + 1;
        }

        return res;
    }
}

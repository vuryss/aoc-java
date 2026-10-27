package com.vuryss.aoc.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Combinatorics {
    /**
     * In combinations the order of element is not important -- (A,B) == (B,A) - one combination
     */
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

    /**
     * In permutations the order of element is important -- (A,B) != (B,A) - two different permutations
     */
    public static <T> List<List<T>> permutations(List<T> a, int k) {
        int n = a.size();
        if (k <= 0 || k > n) return List.of();

        List<List<T>> res = new ArrayList<>();
        boolean[] used = new boolean[n];
        Deque<T> current = new ArrayDeque<>(k);

        backtrackPerm(a, k, used, current, res);
        return res;
    }

    private static <T> void backtrackPerm(List<T> a, int k, boolean[] used, Deque<T> current, List<List<T>> res) {
        if (current.size() == k) {
            res.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < a.size(); i++) {
            if (used[i]) continue;
            used[i] = true;
            current.addLast(a.get(i));

            backtrackPerm(a, k, used, current, res);

            current.removeLast();
            used[i] = false;
        }
    }
}

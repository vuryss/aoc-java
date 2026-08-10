package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    private final static HashMap<Integer, Long> cache = new HashMap<>();

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            """,
            "35",
            """
            28
            33
            18
            42
            31
            14
            46
            20
            48
            47
            24
            23
            49
            45
            19
            38
            39
            11
            1
            32
            25
            35
            8
            17
            7
            9
            4
            2
            34
            10
            3
            """,
            "220"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            """,
            "8",
            """
            28
            33
            18
            42
            31
            14
            46
            20
            48
            47
            24
            23
            49
            45
            19
            38
            39
            11
            1
            32
            25
            35
            8
            17
            7
            9
            4
            2
            34
            10
            3
            """,
            "19208"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var ints = StringUtil.ints(input);
        ints.add(0);
        ints.sort(Integer::compareTo);
        ints.add(ints.getLast() + 3);
        int ones = 0, threes = 0;

        for (var i = 0; i < ints.size() - 1; i++) {
            int current = ints.get(i);
            int next = ints.get(i + 1);
            if (next == current + 1) ones++;
            else if (next == current + 3) threes++;
        }

        return (ones * threes) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var ints = StringUtil.ints(input);
        ints.sort(Integer::compareTo);
        ints.add(ints.getLast() + 3);

        cache.clear();
        return combinations(0, 0, ints) + "";
    }

    private long combinations(int input, int nextIndex, List<Integer> adapters) {
        // Base case
        if (nextIndex == adapters.size()) return 1;

        if (cache.containsKey(nextIndex)) return cache.get(nextIndex);

        long possible = 0;

        for (int i = nextIndex; i < adapters.size(); i++) {
            var adapter = adapters.get(i);
            if (adapter > input + 3) break;
            possible += combinations(adapter, i + 1, adapters);
        }

        cache.put(nextIndex, possible);

        return possible;
    }
}

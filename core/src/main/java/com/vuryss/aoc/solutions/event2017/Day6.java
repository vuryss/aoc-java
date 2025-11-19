package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("0 2 7 0", "5");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("0 2 7 0", "4");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var banks = StringUtil.ints(input);
        var seen = new HashSet<String>();
        int steps = 0, max, index, numBanks = banks.size();

        seen.add(banks.toString());

        while (true) {
            max = 0;
            index = 0;
            steps++;

            for (var i = 0; i < numBanks; i++) {
                if (max < banks.get(i)) {
                    max = banks.get(i);
                    index = i;
                }
            }

            banks.set(index, 0);

            while (max > 0) {
                index = (index + 1) % numBanks;
                banks.set(index, banks.get(index) + 1);
                max--;
            }

            if (!seen.add(banks.toString())) {
                return String.valueOf(steps);
            }
        }
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var banks = StringUtil.ints(input);
        var seen = new HashMap<String, Integer>();
        int steps = 0, max, index, numBanks = banks.size();

        seen.put(banks.toString(), 0);

        while (true) {
            max = 0;
            index = 0;
            steps++;

            for (var i = 0; i < numBanks; i++) {
                if (max < banks.get(i)) {
                    max = banks.get(i);
                    index = i;
                }
            }

            banks.set(index, 0);

            while (max > 0) {
                index = (index + 1) % numBanks;
                banks.set(index, banks.get(index) + 1);
                max--;
            }

            if (seen.containsKey(banks.toString())) {
                return String.valueOf(steps - seen.get(banks.toString()));
            }

            seen.put(banks.toString(), steps);
        }
    }
}

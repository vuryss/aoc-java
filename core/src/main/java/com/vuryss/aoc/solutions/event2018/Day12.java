package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            initial state: #..#.#..##......###...###
            
            ...## => #
            ..#.. => #
            .#... => #
            .#.#. => #
            .#.## => #
            .##.. => #
            .#### => #
            #.#.# => #
            #.### => #
            ##.#. => #
            ##.## => #
            ###.. => #
            ###.# => #
            ####. => #
            """,
            "325"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return sumPots(input, 20);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return sumPots(input, 50_000_000_000L);
    }

    private String sumPots(String input, long generations) {
        var parts = input.trim().split("\n\n");
        var initialState = parts[0].split(" ")[2];
        var rules = new HashMap<String, Character>();

        for (var line: parts[1].split("\n")) {
            var parts2 = line.split(" => ");
            rules.put(parts2[0], parts2[1].charAt(0));
        }

        char[] state = new char[2000];
        System.arraycopy(initialState.toCharArray(), 0, state, 1000, initialState.length());

        int firstPlant = 1000, lastPlant = 1000 + initialState.length();
        char[] newState;
        long[] increments = new long[] { 999, 999, 999 };
        long sum, lastSum = 0;

        for (var i = 0L; i < generations; i++) {
            newState = new char[2000];

            for (var j = firstPlant - 2; j < lastPlant + 2; j++) {
                var key = new StringBuilder();

                for (var k = j - 2; k <= j + 2; k++) {
                    key.append(state[k] == '\0' ? '.' : state[k]);
                }

                newState[j] = rules.getOrDefault(key.toString(), '.');

                if (newState[j] == '#') {
                    if (j < firstPlant) firstPlant = j;
                    if (j > lastPlant) lastPlant = j;
                }
            }

            state = newState;
            sum = sumPots(state, firstPlant, lastPlant);
            increments[(int) i % 3] = sum - lastSum;
            lastSum = sum;

            if (increments[0] == increments[1] && increments[1] == increments[2]) {
                return String.valueOf((generations - i - 1) * increments[0] + sum);
            }
        }

        return String.valueOf(sumPots(state, firstPlant, lastPlant));
    }

    private long sumPots(char[] state, int firstPlant, int lastPlant) {
        long sum = 0;

        for (var i = firstPlant; i <= lastPlant; i++) {
            if (state[i] == '#') {
                sum += i - 1000;
            }
        }

        return sum;
    }
}

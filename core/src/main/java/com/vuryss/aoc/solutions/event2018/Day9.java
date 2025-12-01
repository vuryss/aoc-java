package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.ArrayUtil;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "9 players; last marble is worth 25 points", "32",
            "10 players; last marble is worth 1618 points", "8317",
            "13 players; last marble is worth 7999 points", "146373",
            "17 players; last marble is worth 1104 points", "2764",
            "21 players; last marble is worth 6111 points", "54718",
            "30 players; last marble is worth 5807 points", "37305"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var data = StringUtil.ints(input);
        var players = data.get(0);
        var lastMarble = data.get(1);

        return String.valueOf(playMarbleGame(players, lastMarble));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var data = StringUtil.ints(input);
        var players = data.get(0);
        var lastMarble = data.get(1) * 100;

        return String.valueOf(playMarbleGame(players, lastMarble));
    }

    private long playMarbleGame(int players, int lastMarble) {
        var next = new int[lastMarble + 1];
        var prev = new int[lastMarble + 1];
        var scores = new long[players];

        next[0] = 0;
        prev[0] = 0;

        int current = 0, player;

        for (var i = 1; i <= lastMarble; i++) {
            if (i % 23 == 0) {
                player = i % players;
                scores[player] += i;

                // Move 7 steps back and add the score
                var remove = current;

                for (var j = 0; j < 7; j++) {
                    remove = prev[remove];
                }

                scores[player] += remove;

                // Remove the marble
                var p = prev[remove];
                var n = next[remove];
                next[p] = n;
                prev[n] = p;

                current = n;
            } else {
                // Insert between 1 and 2 clockwise
                var oneClockwise = next[current];
                var twoClockwise = next[oneClockwise];

                next[oneClockwise] = i;
                prev[i] = oneClockwise;

                next[i] = twoClockwise;
                prev[twoClockwise] = i;

                current = i;
            }
        }

        return ArrayUtil.max(scores);
    }
}

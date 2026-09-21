package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import org.jgrapht.alg.util.Pair;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Player 1:
            9
            2
            6
            3
            1
            
            Player 2:
            5
            8
            4
            7
            10
            """,
            "306"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Player 1:
            9
            2
            6
            3
            1
            
            Player 2:
            5
            8
            4
            7
            10
            """,
            "291"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var desks = input.split("\n\n");
        var desk1 = new ArrayDeque<>(
            Arrays.stream(desks[0].split("\n")).skip(1).mapToInt(Integer::parseInt).boxed().toList()
        );
        var desk2 = new ArrayDeque<>(
            Arrays.stream(desks[1].split("\n")).skip(1).mapToInt(Integer::parseInt).boxed().toList()
        );

        while (!desk1.isEmpty() && !desk2.isEmpty()) {
            int card1 = desk1.removeFirst();
            int card2 = desk2.removeFirst();

            if (card1 > card2) {
                desk1.addLast(card1);
                desk1.addLast(card2);
            } else {
                desk2.addLast(card2);
                desk2.addLast(card1);
            }
        }

        var wonDesk = desk1.isEmpty() ? desk2 : desk1;
        int score = 0;

        for (int i = 1, s = wonDesk.size(); i <= s; i++) score += i * wonDesk.removeLast();

        return score + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var desks = input.split("\n\n");
        var desk1 = new ArrayDeque<>(
            Arrays.stream(desks[0].split("\n")).skip(1).mapToInt(Integer::parseInt).boxed().toList()
        );
        var desk2 = new ArrayDeque<>(
            Arrays.stream(desks[1].split("\n")).skip(1).mapToInt(Integer::parseInt).boxed().toList()
        );

        var wonDesk = playRecursiveCombat(desk1, desk2, true).winningDesk;
        int score = 0;

        for (int i = 1, s = wonDesk.size(); i <= s; i++) score += i * wonDesk.removeLast();

        return score + "";
    }

    private static State playRecursiveCombat(Deque<Integer> desk1, Deque<Integer> desk2, boolean topLevel) {
        if (!topLevel) {
            // Optimization: If player 1 holds the max-value card he can never lose it and will always win the game.
            // As we don't care about card order of sub-games, and they do not affect the top-level game, we can stop here.
            var max1 = desk1.stream().mapToInt(Integer::intValue).max().orElseThrow();
            var max2 = desk2.stream().mapToInt(Integer::intValue).max().orElseThrow();

            if (max1 > max2) {
                return new State(true, desk1);
            }
        }

        var states = new HashSet<Pair<ArrayList<Integer>, ArrayList<Integer>>>();

        while (!desk1.isEmpty() && !desk2.isEmpty()) {
            var cacheKey = new Pair<>(new ArrayList<>(desk1), new ArrayList<>(desk2));

            if (!states.add(cacheKey)) return new State(true, desk1);

            var player1card = desk1.removeFirst();
            var player2card = desk2.removeFirst();
            boolean isPlayer1Winning;

            if (player1card <= desk1.size() && player2card <= desk2.size()) {
                isPlayer1Winning = playRecursiveCombat(
                    copyFirst(desk1, player1card),
                    copyFirst(desk2, player2card),
                    false
                ).isPlayer1Winning;
            } else {
                isPlayer1Winning = player1card > player2card;
            }

            if (isPlayer1Winning) {
                desk1.addLast(player1card);
                desk1.addLast(player2card);
            } else {
                desk2.addLast(player2card);
                desk2.addLast(player1card);
            }
        }

        return new State(desk2.isEmpty(), desk1.isEmpty() ? desk2 : desk1);
    }

    private record State(boolean isPlayer1Winning, Deque<Integer> winningDesk) {}

    private static Deque<Integer> copyFirst(Deque<Integer> deck, int count) {
        var copy = new ArrayDeque<Integer>(count);
        var it = deck.iterator();

        for (var i = 0; i < count; i++) copy.addLast(it.next());

        return copy;
    }
}

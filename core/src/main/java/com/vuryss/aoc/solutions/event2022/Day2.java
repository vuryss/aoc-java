package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.Map;

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    private final int ROCK_INDEX = 0;
    private final int PAPER_INDEX = 1;
    private final int SCISSORS_INDEX = 2;

    private final int ROCK_SCORE = 1;
    private final int PAPER_SCORE = 2;
    private final int SCISSORS_SCORE = 3;

    private final int LOSE_SCORE = 0;
    private final int DRAW_SCORE = 3;
    private final int WIN_SCORE = 6;

    // First index - their move (0 rock, 1 paper, 2 scissors)
    // Second index - our move (0 rock, 1 paper, 2 scissors)
    private final Integer[][] outcomes = {
        {ROCK_SCORE + DRAW_SCORE, PAPER_SCORE + WIN_SCORE, SCISSORS_SCORE + LOSE_SCORE},
        {ROCK_SCORE + LOSE_SCORE, PAPER_SCORE + DRAW_SCORE, SCISSORS_SCORE + WIN_SCORE},
        {ROCK_SCORE + WIN_SCORE, PAPER_SCORE + LOSE_SCORE, SCISSORS_SCORE + DRAW_SCORE},
    };

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                A Y
                B X
                C Z
                """,
            "15"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                A Y
                B X
                C Z
                """,
            "12"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var rounds = input.trim().split("\n");
        var score = 0;

        for (var round : rounds) {
            var moves = round.split(" ");
            score += getRoundScore(moves[0].charAt(0), moves[1].charAt(0));
        }

        return String.valueOf(score);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var rounds = input.trim().split("\n");
        var score = 0;

        for (var round : rounds) {
            var moves = round.split(" ");
            score += getRound2Score(moves[0].charAt(0), moves[1].charAt(0));
        }

        return String.valueOf(score);
    }

    private int getRoundScore(Character a, Character b) {
        Map<Character, Integer> theirIndex = Map.of('A', ROCK_INDEX, 'B', PAPER_INDEX, 'C', SCISSORS_INDEX);
        Map<Character, Integer> ourIndex = Map.of('X', ROCK_INDEX, 'Y', PAPER_INDEX, 'Z', SCISSORS_INDEX);

        return outcomes[theirIndex.get(a)][ourIndex.get(b)];
    }

    private int getRound2Score(Character a, Character b) {
        int score = 0;

        if (a == 'A') {
            if (b == 'X') { // Need to lose to rock, so pick scissors and lose (3 + 0)
                score += outcomes[ROCK_INDEX][SCISSORS_INDEX];
            } else if (b == 'Y') { // Need to draw to rock, so pick rock and draw (1 + 3)
                score += outcomes[ROCK_INDEX][ROCK_INDEX];
            } else { // Need to win to rock, so pick paper and win (2 + 6)
                score += outcomes[ROCK_INDEX][PAPER_INDEX];
            }
        } else if (a == 'B') {
            if (b == 'X') { // Need to lose to paper, so pick rock and lose (1 + 0)
                score += outcomes[PAPER_INDEX][ROCK_INDEX];
            } else if (b == 'Y') { // Need to draw to paper, so pick paper and draw (2 + 3)
                score += outcomes[PAPER_INDEX][PAPER_INDEX];
            } else { // Need to win to paper, so pick scissors and win (3 + 6)
                score += outcomes[PAPER_INDEX][SCISSORS_INDEX];
            }
        } else {
            if (b == 'X') { // Need to lose to scissors, so pick paper and lose (2 + 0)
                score += outcomes[SCISSORS_INDEX][PAPER_INDEX];
            } else if (b == 'Y') { // Need to draw to scissors, so pick scissors and draw (3 + 3)
                score += outcomes[SCISSORS_INDEX][SCISSORS_INDEX];
            } else { // Need to win to scissors, so pick rock and win (1 + 6)
                score += outcomes[SCISSORS_INDEX][ROCK_INDEX];
            }
        }

        return score;
    }
}

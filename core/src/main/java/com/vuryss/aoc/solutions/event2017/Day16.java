package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "s1,x3/4,pe/b",
            "baedc"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var programs = (isTest ? "abcde" : "abcdefghijklmnop").toCharArray();
        var moves = parseMoves(input);

        return new String(runDance(programs, moves));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var programs = "abcdefghijklmnop".toCharArray();
        var moves = parseMoves(input);
        var states = new HashMap<String, Integer>();
        states.put(new String(programs), 0);

        for (var i = 0; i < 1_000_000_000; i++) {
            runDance(programs, moves);

            if (states.containsKey(new String(programs))) {
                break;
            }

            states.put(new String(programs), i + 1);
        }

        var cycleLength = states.size();
        var targetIndex = 1_000_000_000 % cycleLength;

        for (var es: states.entrySet()) {
            if (es.getValue() == targetIndex) {
                return es.getKey();
            }
        }

        return "-not found-";
    }

    private Move[] parseMoves(String input) {
        var inputMoves = input.trim().split(",");
        var moves = new Move[inputMoves.length];

        for (var i = 0; i < inputMoves.length; i++) {
            var move = inputMoves[i];

            if (move.startsWith("s")) {
                moves[i] = new Move(MoveType.SPIN, Integer.parseInt(move.substring(1)), 0);
            } else if (move.startsWith("x")) {
                var parts = move.substring(1).split("/");
                moves[i] = new Move(MoveType.EXCHANGE, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } else if (move.startsWith("p")) {
                var parts = move.substring(1).split("/");
                moves[i] = new Move(MoveType.PARTNER, parts[0].charAt(0), parts[1].charAt(0));
            }
        }

        return moves;
    }

    private char[] runDance(char[] programs, Move[] moves) {
        for (var move: moves) {
            switch (move.type) {
                case SPIN -> {
                    var temp = new char[move.a];
                    System.arraycopy(programs, programs.length - move.a, temp, 0, move.a);
                    System.arraycopy(programs, 0, programs, move.a, programs.length - move.a);
                    System.arraycopy(temp, 0, programs, 0, move.a);
                }
                case EXCHANGE -> {
                    var temp = programs[move.a];
                    programs[move.a] = programs[move.b];
                    programs[move.b] = temp;
                }
                case PARTNER -> {
                    var indexA = -1;
                    var indexB = -1;

                    for (var i = 0; i < programs.length; i++) {
                        if (programs[i] == move.a) {
                            indexA = i;
                        } else if (programs[i] == move.b) {
                            indexB = i;
                        }
                    }

                    var temp = programs[indexA];
                    programs[indexA] = programs[indexB];
                    programs[indexB] = temp;
                }
            }
        }

        return programs;
    }

    private enum MoveType {
        SPIN, EXCHANGE, PARTNER
    }

    private record Move(MoveType type, int a, int b) {}
}

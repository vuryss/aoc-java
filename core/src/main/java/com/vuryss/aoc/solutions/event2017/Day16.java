package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
        var programPositionsCycle = new HashMap<Character, ArrayList<Integer>>();
        var foundProgramPositionCycle = new HashSet<Character>();

        for (var program: programs) {
            programPositionsCycle.put(program, new ArrayList<>());
        }

        for (var pos = 0; pos < programs.length; pos++) {
            programPositionsCycle.get(programs[pos]).add(pos);
        }

        for (var i = 0; i < 1_000_000_000; i++) {
            runDance(programs, moves);

            for (var pos = 0; pos < programs.length; pos++) {
                if (!foundProgramPositionCycle.contains(programs[pos])) {
                    programPositionsCycle.get(programs[pos]).add(pos);
                }
            }

            if (i > 1 && i % 2 == 0) {
                int middle = (i + 2) / 2;

                // Check for each character if we have a loop of positions
                for (var ch: programs) {
                    var positions = programPositionsCycle.get(ch);

                    if (
                        !foundProgramPositionCycle.contains(ch)
                        && positions.subList(0, middle).equals(positions.subList(middle, i + 2))
                    ) {
                        foundProgramPositionCycle.add(ch);
                        programPositionsCycle.get(ch).subList(middle, i + 2).clear();
                    }
                }
            }

            if (foundProgramPositionCycle.size() == programs.length) {
                break;
            }
        }

        for (var es: programPositionsCycle.entrySet()) {
            var charCycleIndex = 1_000_000_000 % es.getValue().size();
            programs[es.getValue().get(charCycleIndex)] = es.getKey();
        }

        return new String(programs);
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

package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.ArrayList;
import java.util.Map;


@SuppressWarnings("unused")
public class Day4 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
            
            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19
            
             3 15  0  2 22
             9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6
            
            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
             2  0 12  3  7
            """,
            "4512"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
            
            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19
            
             3 15  0  2 22
             9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6
            
            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
             2  0 12  3  7
            """,
            "1924"
        );
    }

    @Override
    public String part1Solution(String input) {
        var parts = input.split("\n\n");
        var numbers = StringUtil.ints(parts[0]);
        var boards = parseBoards(input);

        for (var number: numbers) {
            for (var board : boards) {
                board.markNumber(number);

                if (board.isWinner()) {
                    return String.valueOf(number * board.getSumOfUnmarked());
                }
            }
        }

        return "";
    }

    @Override
    public String part2Solution(String input) {
        var parts = input.split("\n\n");
        var numbers = StringUtil.ints(parts[0]);
        var boards = parseBoards(input);
        var boardsLeft = new ArrayList<Board>();

        for (var number: numbers) {
            Board removeBoard = null;

            for (var board : boards) {
                board.markNumber(number);

                if (board.isWinner()) {
                    if (boards.size() == 1) {
                        return String.valueOf(number * board.getSumOfUnmarked());
                    }

                    continue;
                }

                boardsLeft.add(board);
            }

            boards = boardsLeft;
            boardsLeft = new ArrayList<>();
        }

        return "";
    }

    private ArrayList<Board> parseBoards(String input) {
        var parts = input.split("\n\n");
        var boards = new ArrayList<Board>();

        for (var i = 1; i < parts.length; i++) {
            var lines = parts[i].split("\n");
            var board = new int[5][5];

            for (var line = 0; line < 5; line++) {
                var lineNumbers = StringUtil.ints(lines[line]);

                for (var charIndex = 0; charIndex < lineNumbers.size(); charIndex++) {
                    board[line][charIndex] = lineNumbers.get(charIndex);
                }
            }

            boards.add(new Board(board));
        }

        return boards;
    }
}

class Board {
    private final int[][] board;
    private final int[][] markedBoard;

    public Board (int[][] board) {
        this.board = board;
        this.markedBoard = new int[5][5];
    }

    public void markNumber(int number) {
        for (var line = 0; line < 5; line++) {
            for (var charIndex = 0; charIndex < 5; charIndex++) {
                if (board[line][charIndex] == number) {
                    markedBoard[line][charIndex] = 1;
                }
            }
        }
    }

    public boolean isWinner() {
        for (var i = 0; i < 5; i++) {
            int lineSum = 0, charSum = 0;

            for (var j = 0; j < 5; j++) {
                lineSum += markedBoard[i][j];
                charSum += markedBoard[j][i];
            }

            if (lineSum == 5 || charSum == 5) {
                return true;
            }
        }

        return false;
    }

    public int getSumOfUnmarked() {
        int sum = 0;

        for (var line = 0; line < 5; line++) {
            for (var charIndex = 0; charIndex < 5; charIndex++) {
                if (markedBoard[line][charIndex] == 0) {
                    sum += board[line][charIndex];
                }
            }
        }

        return sum;
    }
}
package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.List;
import java.util.Map;

public class Day10 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop
            """,
            "13140"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop
            """,
            ""
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.split("\n");
        var x = 1;
        var cycle = 1;
        var sum = 0;

        for (var line: lines) {
            var parts = line.split(" ");

            if (parts[0].equals("noop")) {
                sum += addIfCycleMatch(cycle, x);
                cycle++;
            } else if (parts[0].equals("addx")) {
                sum += addIfCycleMatch(cycle, x);
                cycle++;
                sum += addIfCycleMatch(cycle, x);
                cycle++;
                x += Integer.parseInt(parts[1]);
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.split("\n");
        var x = 1;
        var cycle = 0;
        char[][] grid = new char[6][40];

        for (var line: lines) {
            var parts = line.split(" ");

            if (parts[0].equals("noop")) {
                printPixel(grid, cycle, x);
                cycle++;
            } else if (parts[0].equals("addx")) {
                printPixel(grid, cycle, x);
                cycle++;
                printPixel(grid, cycle, x);
                cycle++;
                x += Integer.parseInt(parts[1]);
            }
        }

        System.out.println();

        for (var line: grid) {
            for (var column: line) {
                System.out.print(column);
            }
            System.out.println();
        }

        return "";
    }

    private int addIfCycleMatch(int cycle, int x) {
        var cycles = List.of(20, 60, 100, 140, 180, 220);

        return cycles.contains(cycle) ? cycle * x : 0;
    }

    private void printPixel(char[][] grid, int cycle, int x) {
        var row = cycle / 40;
        var column = cycle % 40;

        grid[row][column] = x >= column - 1 && x <= column + 1 ? '#' : ' ';
    }
}

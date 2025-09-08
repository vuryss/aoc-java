package com.vuryss.aoc.cli.output;

public interface OutputInterface {
    void note(String message);

    void part1(String result, double executionTime);
    void part1(String result, double executionTime, boolean correct);

    void part2(String result, double executionTime);
    void part2(String result, double executionTime, boolean correct);

    void testPart1(String result, String expected, boolean success);
    void testPart2(String result, String expected, boolean success);

    void flush();
}

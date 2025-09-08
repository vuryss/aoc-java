package com.vuryss.aoc.cli.output;

import picocli.CommandLine;

public class PrettyPrintOutput implements  OutputInterface {
    @Override
    public void note(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "@|bold,blue %s |@\n", message
        )));
    }

    @Override
    public void part1(String result, double executionTime) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 1 result: @|bold,green %s|@ (executed in %.2f ms)\n",
            result,
            executionTime
        )));
    }

    @Override
    public void part1(String result, double executionTime, boolean correct) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 1 result: @|bold,cyan %s|@ @|bold,%s %s|@ (executed in %.2f ms)\n",
            result,
            correct ? "green" : "red",
            correct ? "CORRECT" : "INCORRECT",
            executionTime
        )));
    }

    @Override
    public void part2(String result, double executionTime) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 2 result: @|bold,green %s|@ (executed in %.2f ms)\n",
            result,
            executionTime
        )));
    }

    @Override
    public void part2(String result, double executionTime, boolean correct) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 2 result: @|bold,cyan %s|@ @|bold,%s %s|@ (executed in %.2f ms)\n",
            result,
            correct ? "green" : "red",
            correct ? "CORRECT" : "INCORRECT",
            executionTime
        )));
    }

    @Override
    public void testPart1(String result, String expected, boolean success) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 1 Test: @|bold,%s %s|@ (result: %s, expected: %s)",
            success ? "green" : "red",
            success ? "PASSED" : "FAILED",
            result,
            expected
        )));
    }

    @Override
    public void testPart2(String result, String expected, boolean success) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format(
            "\tPart 2 Test: @|bold,%s %s|@ (result: %s, expected: %s)",
            success ? "green" : "red",
            success ? "PASSED" : "FAILED",
            result,
            expected
        )));
    }

    @Override
    public void flush() {
        // No-op
    }
}

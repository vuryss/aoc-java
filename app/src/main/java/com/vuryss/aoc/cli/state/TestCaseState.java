package com.vuryss.aoc.cli.state;

public record TestCaseState(
    int part,
    int number,
    String input,
    String expected,
    PartState state
) {
    public TestCaseState withState(PartState newState) {
        return new TestCaseState(part, number, input, expected, newState);
    }
}

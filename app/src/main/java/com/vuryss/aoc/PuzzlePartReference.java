package com.vuryss.aoc;

public record PuzzlePartReference(
    int event,
    int day,
    int part
) {
    public PuzzlePartReference(PuzzleDayReference puzzle, int part) {
        this(puzzle.event(), puzzle.day(), part);
    }

    public PuzzleDayReference puzzle() {
        return new PuzzleDayReference(this.event, this.day);
    }
}

package com.vuryss.aoc.cli.json;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cli.display.DayState;

import java.util.ArrayList;
import java.util.List;

record SingleDayOutputDto(
    int event,
    int day,
    String part1Result,
    String part2Result,
    double part1ExecutionTimeMs,
    double part2ExecutionTimeMs,
    String part1Error,
    String part2Error,
    String part1ExpectedResult,
    String part2ExpectedResult,
    List<String> errors,
    List<String> notices
) {
    public static SingleDayOutputDto from(PuzzleDayReference puzzle, DayState dayState) {
        return new SingleDayOutputDto(
            puzzle.event(),
            puzzle.day(),
            dayState.part1().result(),
            dayState.part2().result(),
            dayState.part1().elapsedMs(),
            dayState.part2().elapsedMs(),
            dayState.part1().error(),
            dayState.part2().error(),
            dayState.part1().expectedResult(),
            dayState.part2().expectedResult(),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public SingleDayOutputDto withErrors(List<String> errors) {
        return new SingleDayOutputDto(
            this.event,
            this.day,
            this.part1Result,
            this.part2Result,
            this.part1ExecutionTimeMs,
            this.part2ExecutionTimeMs,
            this.part1Error,
            this.part2Error,
            this.part1ExpectedResult,
            this.part2ExpectedResult,
            errors,
            this.notices
        );
    }

    public SingleDayOutputDto withNotices(List<String> notices) {
        return new SingleDayOutputDto(
            this.event,
            this.day,
            this.part1Result,
            this.part2Result,
            this.part1ExecutionTimeMs,
            this.part2ExecutionTimeMs,
            this.part1Error,
            this.part2Error,
            this.part1ExpectedResult,
            this.part2ExpectedResult,
            this.errors,
            notices
        );
    }
}

package com.vuryss.aoc.cli.json;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cli.state.DayState;

import java.util.ArrayList;
import java.util.List;

record WholeEventOutputDto(
    List<DayDto> days,
    List<String> errors,
    List<String> notices
) {
    public static WholeEventOutputDto from(List<DayDto> dayDtos) {
        return new WholeEventOutputDto(dayDtos, new ArrayList<>(), new ArrayList<>());
    }

    public WholeEventOutputDto withErrors(List<String> errors) {
        return new WholeEventOutputDto(this.days, errors, this.notices);
    }

    public WholeEventOutputDto withNotices(List<String> notices) {
        return new WholeEventOutputDto(this.days, this.errors, notices);
    }

    record DayDto(
        int event,
        int day,
        String part1Result,
        String part2Result,
        double part1ExecutionTimeMs,
        double part2ExecutionTimeMs,
        String part1Error,
        String part2Error,
        String part1ExpectedResult,
        String part2ExpectedResult
    ) {
        public static DayDto from(PuzzleDayReference puzzle, DayState dayState) {
            return new DayDto(
                puzzle.event(),
                puzzle.day(),
                dayState.part1().result(),
                dayState.part2().result(),
                dayState.part1().elapsedMs(),
                dayState.part2().elapsedMs(),
                dayState.part1().error(),
                dayState.part2().error(),
                dayState.part1().expectedResult(),
                dayState.part2().expectedResult()
            );
        }
    }
}

package com.vuryss.aoc.cli.json;

import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.cli.display.TestCaseState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

record SingleDayTestOutputDto(
    int event,
    int day,
    List<PartTestResult> part1Tests,
    List<PartTestResult> part2Tests,
    List<String> errors,
    List<String> notices
) {
    public static SingleDayTestOutputDto from(PuzzleDayReference puzzle, List<TestCaseState> testCasesState) {
        var errors = new ArrayList<String>();

        List<PartTestResult> part1Tests = testCasesState.stream()
            .filter(testState -> testState.part() == 1)
            .map(state -> PartTestResult.from(state, errors))
            .collect(Collectors.toList());

        List<PartTestResult> part2Tests = testCasesState.stream()
            .filter(testState -> testState.part() == 2)
            .map(state -> PartTestResult.from(state, errors))
            .collect(Collectors.toList());

        return new SingleDayTestOutputDto(
            puzzle.event(),
            puzzle.day(),
            part1Tests,
            part2Tests,
            errors,
            new ArrayList<>()
        );
    }

    public SingleDayTestOutputDto addErrors(List<String> moreErrors) {
        var updatedErrors = new ArrayList<>(errors);
        updatedErrors.addAll(moreErrors);

        return new SingleDayTestOutputDto(
            this.event,
            this.day,
            part1Tests,
            part2Tests,
            updatedErrors,
            this.notices
        );
    }

    public SingleDayTestOutputDto addNotices(List<String> moreNotices) {
        var updatedNotices = new ArrayList<>(notices);
        updatedNotices.addAll(moreNotices);

        return new SingleDayTestOutputDto(
            this.event,
            this.day,
            part1Tests,
            part2Tests,
            this.errors,
            updatedNotices
        );
    }

    private record PartTestResult(
        String input,
        String result,
        double elapsedMs,
        String expectedResult
    ) {
        public static PartTestResult from(TestCaseState state, List<String> errors) {
            var error = state.state().error();

            if (error != null && !error.isEmpty()) {
                errors.add(error);
            }

            return new PartTestResult(
                state.input(),
                state.state().result(),
                state.state().elapsedMs(),
                state.state().expectedResult()
            );
        }
    }
}



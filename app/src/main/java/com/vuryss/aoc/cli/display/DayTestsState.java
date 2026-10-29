package com.vuryss.aoc.cli.display;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record DayTestsState(List<TestCaseState> tests) {
    public DayTestsState {
        tests = List.copyOf(tests);
    }

    public DayTestsState withUpdate(TestUpdate change) {
        var copy = new ArrayList<>(tests);
        copy.set(change.index(), copy.get(change.index()).withState(change.state()));
        return new DayTestsState(copy);
    }

    public boolean isFinished() {
        return tests.stream().allMatch(test -> test.state().isFinished());
    }

    public Stream<PartState> parts() {
        return tests.stream().map(TestCaseState::state);
    }
}

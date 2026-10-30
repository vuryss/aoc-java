package com.vuryss.aoc.cli.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record DayTestsState(List<TestCaseState> tests) implements RunState {
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

    public PartState summary(int part) {
        var states = tests.stream().filter(test -> test.part() == part).map(TestCaseState::state).toList();
        long startedAtNs = states.stream().mapToLong(PartState::startedAtNs).filter(t -> t >= 0).min().orElse(-1);
        var summary = PartState.queued(true);

        if (startedAtNs >= 0) summary = summary.started(startedAtNs);
        if (!states.stream().allMatch(PartState::isFinished)) return summary;

        long passed = states.stream()
            .filter(state -> state.validationStatus() == PartState.ValidationStatus.VALID)
            .count();
        var failure = states.stream()
            .filter(state -> state.validationStatus() == PartState.ValidationStatus.ERROR)
            .findFirst();

        var validation = PartState.ValidationStatus.VALID;

        if (states.isEmpty()) validation = PartState.ValidationStatus.UNAVAILABLE;
        else if (passed != states.size()) validation = PartState.ValidationStatus.INVALID;

        long finishedAtNs = states.stream().mapToLong(PartState::finishedAtNs).max().orElse(-1);

        var completed = summary.completed(passed + "/" + states.size(), finishedAtNs);

        return failure
            .map(test -> completed.validationFailed(test.failure()))
            .orElse(completed.withValidation(validation, null, null));
    }
}

package com.vuryss.aoc.cli.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record EventState(List<DayState> days) {
    public static EventState queued(int dayCount, boolean validate) {
        return new EventState(Collections.nCopies(dayCount, DayState.queued(validate)));
    }

    public EventState updateDayState(int day, DayState state) {
        var copy = new ArrayList<>(days);
        copy.set(day - 1, state);
        return new EventState(copy);
    }

    public EventState withUpdate(StateUpdate change) {
        var copy = new ArrayList<>(days);
        var index = change.part().day() - 1;
        var currentDayState = days.get(index);

        copy.set(index, currentDayState.withUpdate(change));

        return new EventState(copy);
    }

    public boolean isFinished() {
        return days.stream().allMatch(DayState::isFinished);
    }

    public long finishedDays() {
        return days.stream().filter(DayState::isFinished).count();
    }
}

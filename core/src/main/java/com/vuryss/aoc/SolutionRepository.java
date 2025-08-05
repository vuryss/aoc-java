package com.vuryss.aoc;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.Optional;

public class SolutionRepository {
    public static Optional<DayInterface> getSolutionFor(int year, int day) {
        var className = String.format("com.vuryss.aoc.solutions.event%s.Day%s", year, day);

        try {
            Class<?> dynamicClass = Class.forName(className);
            return Optional.of((DayInterface) dynamicClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

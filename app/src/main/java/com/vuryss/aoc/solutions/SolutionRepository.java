package com.vuryss.aoc.solutions;

import com.vuryss.aoc.PuzzleDayReference;

public class SolutionRepository {
    private static class Holder {
        public static final SolutionRepository INSTANCE = new SolutionRepository();
    }

    public static SolutionRepository instance() {
        return SolutionRepository.Holder.INSTANCE;
    }

    private SolutionRepository() {}

    public SolutionInterface find(PuzzleDayReference puzzle) {
        String className = "%s.event%s.Day%d".formatted(
            SolutionInterface.class.getPackageName(),
            puzzle.event(),
            puzzle.day()
        );

        try {
            Class<?> clazz = Class.forName(className);

            return clazz.asSubclass(SolutionInterface.class).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception | LinkageError e) {
            throw new RuntimeException("Error creating solution instance for: " + className, e);
        }
    }
}

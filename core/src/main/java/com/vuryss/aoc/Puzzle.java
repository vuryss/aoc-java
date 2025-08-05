package com.vuryss.aoc;

import com.vuryss.aoc.solutions.DayInterface;

public class Puzzle {
    private final int year;
    private final int day;
    private DayInterface solution;
    private long part1ExecutionTime;
    private long part2ExecutionTime;

    public Puzzle(int year, int day) {
        this.year = year;
        this.day = day;

        SolutionRepository.getSolutionFor(year, day).ifPresent(dayInterface -> this.solution = dayInterface);
    }

//    public boolean exists() {
//        return solution != null;
//    }
//
//    public String part1Solution() {
//        failIfNotExists();
//
//        var startTime = System.nanoTime();
//        var result = solution.part1Solution(getInput());
//
//        part1ExecutionTime = (System.nanoTime() - startTime) / 1_000_000;
//
//        return result;
//    }
//
//    public long part1ExecutionTime() {
//        return part1ExecutionTime;
//    }
//
//    public String part2Solution() {
//        failIfNotExists();
//
//        var startTime = System.nanoTime();
//        var result = solution.part2Solution(getInput());
//
//        part2ExecutionTime = (System.nanoTime() - startTime) / 1_000_000;
//
//        return result;
//    }
//
//    public long part2ExecutionTime() {
//        return part2ExecutionTime;
//    }
//
//    private void failIfNotExists() {
//        if (!exists()) {
//            throw new RuntimeException("Solution for year " + year + " day " + day + " not found!");
//        }
//    }
//
//    private String getInput() {
//        return new InputResolver().getForEventDay(year, day);
//    }
}

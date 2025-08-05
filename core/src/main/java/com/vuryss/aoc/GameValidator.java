package com.vuryss.aoc;

import com.vuryss.aoc.solutions.DayInterface;

public class GameValidator {
    public GameValidator(int year, int day, boolean isTest) {
//        try {
//            getEventDaySolution(year, day);
//        } catch (Exception e) {
//            return;
//        }
//
//        System.out.print("Validating for year " + year + " day " + day + (isTest ? " with test data" : "") + "... ");
//
//        if (isTest) {
//            executeWithTestInput(year, day);
//            System.out.println();
//            return;
//        }
//
//        executeWithUserInput(year, day);
//        System.out.println();
    }
//
//    private void executeWithUserInput(int year, int day) {
//        var solution = getEventDaySolution(year, day);
//        var input = new InputResolver().getForEventDay(year, day);
//        var answers = new AnswerResolver().getForEventDay(year, day);
//
//        System.out.print(" ");
//
//        if (solution.part1Solution(input).equals(answers.get(1))) {
//            System.out.print("part 1: success! ");
//        } else {
//            System.out.print("part 1: error! ");
//        }
//
//        if (solution.part2Solution(input).equals(answers.get(2))) {
//            System.out.print("part 2: success!");
//        } else {
//            System.out.print("part 2: error!");
//        }
//    }
//
//    private void executeWithTestInput(int year, int day) {
//        var solution = getEventDaySolution(year, day);
//
//        System.out.print("part 1: ");
//
//        for (var testSet: solution.part1Tests().entrySet()) {
//            var testResult = solution.part1Solution(testSet.getKey());
//
//            if (testResult.equals(testSet.getValue())) {
//                System.out.print("success ");
//            } else {
//                System.out.print("!error! ");
//            }
//        }
//
//        System.out.print("| part2: ");
//
//        for (var testSet: solution.part2Tests().entrySet()) {
//            var testResult = solution.part2Solution(testSet.getKey());
//
//            if (testResult.equals(testSet.getValue())) {
//                System.out.print("success ");
//            } else {
//                System.out.print("!error! ");
//            }
//        }
//    }
//
//    private DayInterface getEventDaySolution(int year, int day) {
//        var className = String.format("com.vuryss.aoc.solutions.event%s.Day%s", year, day);
//
//        try {
//            Class<?> dynamicClass = Class.forName(className);
//            return (DayInterface) dynamicClass.getDeclaredConstructor().newInstance();
//        } catch (Exception e) {
//            throw new RuntimeException(String.format("Solution for event %s day %s not found!", year, day), e);
//        }
//    }
}

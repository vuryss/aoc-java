package com.vuryss.aoc.service;

import com.vuryss.aoc.DayInterface;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class Game {
    private final ApplicationContext applicationContext;
    private final InputResolver inputResolver;

    public Game(ApplicationContext applicationContext, InputResolver inputResolver) {
        this.applicationContext = applicationContext;
        this.inputResolver = inputResolver;
    }

    public void execute(int year, int day, boolean isTest) {
        if (isTest) {
            executeWithTestInput(year, day);
            return;
        }

        executeWithUserInput(year, day);
    }

    private void executeWithTestInput(int year, int day) {
        var solution = getEventDaySolution(year, day);

        System.out.println("Executing tests for part 1...");

        var testNumber = 0;

        for (var testSet: solution.part1Tests().entrySet()) {
            System.out.printf("\tExecuting test %s ... ", ++testNumber);

            var testResult = solution.part1Solution(testSet.getKey());

            if (testResult.equals(testSet.getValue())) {
                System.out.println("success!");
            } else {
                System.out.println("error!");
            }
        }

        System.out.println("Executing tests for part 2...");

        testNumber = 0;

        for (var testSet: solution.part2Tests().entrySet()) {
            System.out.printf("\tExecuting test %s ... ", ++testNumber);

            var testResult = solution.part2Solution(testSet.getKey());

            if (testResult.equals(testSet.getValue())) {
                System.out.println("success!");
            } else {
                System.out.printf("error! Expected: %s | Result: %s\n", testSet.getValue(), testResult);
            }
        }
    }

    private void executeWithUserInput(int year, int day) {
        var solution = getEventDaySolution(year, day);
        var input = inputResolver.downloadForEventDay(year, day);

        System.out.printf("Solution for day %s part 1: %s\n", day, solution.part1Solution(input));
        System.out.printf("Solution for day %s part 2: %s\n", day, solution.part2Solution(input));
    }

    private DayInterface getEventDaySolution(int year, int day) {
        var className = String.format("com.vuryss.aoc.solutions.event%s.Day%s", year, day);

        try {
            return (DayInterface) applicationContext
                .getAutowireCapableBeanFactory()
                .autowire(Class.forName(className), AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, true);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Solution for event %s day %s not found!", year, day), e);
        }
    }
}

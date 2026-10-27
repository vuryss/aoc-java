package com.vuryss.aoc;

import com.vuryss.aoc.cli.NotificationOutputInterface;
import com.vuryss.aoc.data.DataProviderInterface;

import java.util.function.Function;

public interface OutputInterface {
    void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception;

    void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        Function<NotificationOutputInterface, DataProviderInterface> generateDataProvider
    ) throws Exception;
}

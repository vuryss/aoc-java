package com.vuryss.aoc;

import com.vuryss.aoc.data.DataProviderInterface;

public interface OutputInterface {
    void singleDay(
        PuzzleDayReference puzzle,
        boolean isTest,
        boolean validate,
        DataProviderInterface dataProvider
    ) throws Exception;

    void wholeEvent(
        int event,
        boolean isTest,
        boolean validate,
        DataProviderInterface dataProvider
    ) throws Exception;
}

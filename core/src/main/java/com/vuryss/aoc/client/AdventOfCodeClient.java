package com.vuryss.aoc.client;

import java.util.Map;

public interface AdventOfCodeClient {
    String getPlayerInput(int year, int day);
    Answers getCorrectAnswers(int eventYear, int eventDay);
}

package com.vuryss.aoc.client;

public interface AdventOfCodeClient {
    String getPlayerInput(int year, int day);
    Answers getCorrectAnswers(int eventYear, int eventDay);
}

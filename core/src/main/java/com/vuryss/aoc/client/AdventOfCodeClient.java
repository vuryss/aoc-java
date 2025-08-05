package com.vuryss.aoc.client;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface AdventOfCodeClient {
    String downloadInput(int year, int day);

    String downloadAnswer(int year, int day, int part);
}

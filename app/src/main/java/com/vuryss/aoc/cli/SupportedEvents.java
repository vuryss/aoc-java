package com.vuryss.aoc.cli;

import com.vuryss.aoc.Configuration;

import java.util.ArrayList;

public class SupportedEvents extends ArrayList<String> {
    public SupportedEvents() {
        Configuration.getSupportedEvents().stream().sorted().map(String::valueOf).forEach(this::add);
    }
}

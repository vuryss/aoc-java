package com.vuryss.aoc.cli;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class SupportedDays extends ArrayList<String> {
    public SupportedDays() {
        IntStream.rangeClosed(1, 25).mapToObj(String::valueOf).forEach(this::add);
    }
}

package com.vuryss.aoc.solutions;

import java.util.Map;

public interface SolutionInterface {
    Map<String, String> part1Tests();
    Map<String, String> part2Tests();
    String part1Solution(String input);
    String part2Solution(String input);
}

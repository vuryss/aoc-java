package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("unused")
public class Day1 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1000
            2000
            3000
                        
            4000
                        
            5000
            6000
                        
            7000
            8000
            9000
                        
            10000
            """,
            "24000"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            1000
            2000
            3000
                        
            4000
                        
            5000
            6000
                        
            7000
            8000
            9000
                        
            10000
            """,
            "45000"
        );
    }

    @Override
    public String part1Solution(String input) {
        var elfInputs = input.trim().split("\n\n");
        long max = 0L;

        for (var elfInput: elfInputs) {
            long calories = Arrays.stream(elfInput.split("\n")).mapToLong(Long::parseLong).sum();
            max = Math.max(max, calories);
        }

        return String.valueOf(max);
    }

    @Override
    public String part2Solution(String input) {
        var elfInputs = input.trim().split("\n\n");
        var elfCalories = new ArrayList<Long>();

        for (var elfInput: elfInputs) {
            long calories = Arrays.stream(elfInput.split("\n")).mapToLong(Long::parseLong).sum();
            elfCalories.add(calories);
        }

        Collections.sort(elfCalories);
        Collections.reverse(elfCalories);

        return String.valueOf(elfCalories.get(0) + elfCalories.get(1) + elfCalories.get(2));
    }
}

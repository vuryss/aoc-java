package com.vuryss.aoc.cli.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.cli.Result;

public class JsonOutput implements  OutputInterface {
    public String part1;
    public double part1ExecutionTime;

    public String part2;
    public double part2ExecutionTime;

    @Override
    public void note(String message) {
        // No-op
    }

    @Override
    public void part1(String result, double executionTime) {
        this.part1 = result;
        this.part1ExecutionTime = executionTime;
    }

    @Override
    public void part1(String result, double executionTime, boolean correct) {
        this.part1 = result;
        this.part1ExecutionTime = executionTime;
    }

    @Override
    public void part2(String result, double executionTime) {
        this.part2 = result;
        this.part2ExecutionTime = executionTime;
    }

    @Override
    public void part2(String result, double executionTime, boolean correct) {
        this.part2 = result;
        this.part2ExecutionTime = executionTime;
    }

    @Override
    public void testPart1(String result, String expected, boolean success) {
        // No-op
    }

    @Override
    public void testPart2(String result, String expected, boolean success) {
        // No-op
    }

    @Override
    public void flush() {
        try {
            System.out.println(
                new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(this)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

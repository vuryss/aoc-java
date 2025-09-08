package com.vuryss.aoc.api.process;

public record ProcessResult(String stdout, String stderr, int exitCode) {}

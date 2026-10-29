package com.vuryss.aoc.cli.state;

import com.vuryss.aoc.PuzzlePartReference;

import java.util.function.UnaryOperator;

public record StateUpdate(PuzzlePartReference part, UnaryOperator<PartState> update) {}

package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day5Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 5;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("ugknbfddgicrmopn", new Answer("1", "0")),
            Arguments.of("aaa", new Answer("1", "0")),
            Arguments.of("jchzalrnumimnmhp", new Answer("0", "0")),
            Arguments.of("haegwjzuvuyypxyu", new Answer("0", "0")),
            Arguments.of("dvszwmarrgswjxmb", new Answer("0", "0")),
            Arguments.of("qjhvhtzxzqqjkmpb", new Answer("0", "1")),
            Arguments.of("xxyxx", new Answer("0", "1")),
            Arguments.of("uurcxstgmygtbstg", new Answer("0", "0")),
            Arguments.of("ieodomkazucvgmuy", new Answer("0", "0")),
            Arguments.of("ugknbfddgicrmopn\naaa", new Answer("2", "0")),
            Arguments.of("qjhvhtzxzqqjkmpb\nxxyxx", new Answer("0", "2"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("ABC"), // Uppercase not allowed
            Arguments.of("abc123"), // Numbers not allowed
            Arguments.of("abc-def"), // Hyphen not allowed
            Arguments.of("abc def"), // Space not allowed
            Arguments.of("abc@def"), // Special character not allowed
            Arguments.of("aaa\nAAA"), // Uppercase in multi-line
            Arguments.of("aaa\n123"), // Numbers in multi-line
            Arguments.of("a".repeat(20001)) // Too long
        );
    }
}


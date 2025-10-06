package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day4Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 4;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("abcdef", new Answer("609043", "6742839")),
            Arguments.of("pqrstuv", new Answer("1048970", "5714438")),
            Arguments.of("abc123", new Answer("2886342", "22988540")),
            Arguments.of("test", new Answer("2571334", "9159751")),
            Arguments.of("a", new Answer("12181", "14779946"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc def"), // Space not allowed
            Arguments.of("abc-def"), // Hyphen not allowed
            Arguments.of("abc_def"), // Underscore not allowed
            Arguments.of("abc@def"), // Special character not allowed
            Arguments.of("abcdefghijk"), // Too long (>10 chars)
            Arguments.of("a".repeat(11)), // Too long
            Arguments.of("test\ntest") // Newline not allowed
        );
    }
}


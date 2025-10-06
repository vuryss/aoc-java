package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day3Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 3;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(">", new Answer("2", "2")),
            Arguments.of("^>v<", new Answer("4", "3")),
            Arguments.of("^v^v^v^v^v", new Answer("2", "11")),
            Arguments.of("^v", new Answer("2", "3")),
            Arguments.of(">>><<<", new Answer("4", "4")),
            Arguments.of("^>v<^>v<", new Answer("4", "3"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("^>v<abc"),
            Arguments.of("123"),
            Arguments.of("^>v< "),
            Arguments.of("^>v<\n"),
            Arguments.of("ABCD"),
            Arguments.of("^>v<@#$"),
            Arguments.of("a".repeat(10001)) // Too long
        );
    }
}


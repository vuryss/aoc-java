package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day20Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 20;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("150", new Answer("8", "8")),
            Arguments.of("70", new Answer("4", "4")),
            Arguments.of("120", new Answer("6", "6")),
            Arguments.of("33100000", new Answer("776160", "786240")),
            Arguments.of("36000000", new Answer("831600", "884520"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("150\n200"),
            Arguments.of("150.5"),
            Arguments.of("-150"),
            Arguments.of("12345678901")
        );
    }
}


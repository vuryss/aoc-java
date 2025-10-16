package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day10Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 10;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("1113122113", new Answer("360154", "5103798")),
            Arguments.of("1113222113", new Answer("252594", "3579328"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("0"), // 0 not allowed
            Arguments.of("1 2 3"), // Spaces not allowed
            Arguments.of("1\n2"), // Newlines not allowed
            Arguments.of("1".repeat(101)) // Too long
        );
    }
}


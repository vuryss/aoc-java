package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day11Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 11;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("abcdefgh", new Answer("abcdffaa", "abcdffbb")),
            Arguments.of("vzbxkghb", new Answer("vzbxxyzz", "vzcaabcc")),
            Arguments.of("hxbxwxba", new Answer("hxbxxyzz", "hxcaabcc"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("abcd"), // Too short
            Arguments.of("abcdefgasdasdasdashi"), // Too long
            Arguments.of("ABCDEFGH"), // Uppercase not allowed
            Arguments.of("abcdefg1"), // Digits not allowed
            Arguments.of("abcdef h"), // Space not allowed
            Arguments.of("abcdef-h") // Special chars not allowed
        );
    }
}


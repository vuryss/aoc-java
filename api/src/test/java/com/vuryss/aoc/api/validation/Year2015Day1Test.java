package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day1Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 1;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("(())", new Answer("0", "Santa never enters the basement")),
            Arguments.of("()()", new Answer("0", "Santa never enters the basement")),
            Arguments.of("(((", new Answer("3", "Santa never enters the basement")),
            Arguments.of("(()(()(", new Answer("3", "Santa never enters the basement")),
            Arguments.of("))))((((", new Answer("0", "1")),
            Arguments.of("())", new Answer("-1", "3")),
            Arguments.of("))(", new Answer("-1", "1")),
            Arguments.of(")))", new Answer("-3", "1")),
            Arguments.of(")())())", new Answer("-3", "1"))
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("()abc"),
            Arguments.of("abc()"),
            Arguments.of("((((()) "),
            Arguments.of("%((())))))")
        );
    }
}


package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day17Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 17;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                20
                15
                10
                5
                5
                95
                """,
                new Answer("1", "1")
            ),
            Arguments.of(
                """
                50
                44
                11
                49
                42
                46
                18
                32
                26
                40
                21
                7
                18
                43
                10
                47
                36
                24
                22
                40
                """,
                new Answer("654", "57")
            ),
            Arguments.of(
                """
                33
                14
                18
                20
                45
                35
                16
                35
                1
                13
                18
                13
                50
                44
                48
                6
                24
                41
                30
                42
                """,
                new Answer("1304", "18")
            )
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("20\nabc\n10"),
            Arguments.of("20 15 10"),
            Arguments.of("20.5\n15.3"),
            Arguments.of("-20\n15"),
            Arguments.of("a".repeat(1001))
        );
    }
}


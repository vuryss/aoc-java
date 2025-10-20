package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day24Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 24;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                1
                2
                3
                4
                5
                7
                8
                9
                10
                11
                """,
                new Answer("99", "44")
            ),
            Arguments.of(
                """
                1
                3
                5
                11
                13
                17
                19
                23
                29
                31
                37
                41
                43
                47
                53
                59
                67
                71
                73
                79
                83
                89
                97
                101
                103
                107
                109
                113
                """,
                new Answer("10439961859", "72050269")
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
            Arguments.of("1\nabc\n3"),
            Arguments.of("1 2 3"),
            Arguments.of("1.5\n2.3"),
            Arguments.of("-1\n2"),
            Arguments.of("a".repeat(1001))
        );
    }
}


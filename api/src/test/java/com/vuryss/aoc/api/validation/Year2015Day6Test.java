package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day6Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 6;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of("turn on 0,0 through 999,999", new Answer("1000000", "1000000")),
            Arguments.of("toggle 0,0 through 999,0", new Answer("1000", "2000")),
            Arguments.of("turn off 499,499 through 500,500", new Answer("0", "0")),
            Arguments.of("turn on 0,0 through 0,0", new Answer("1", "1")),
            Arguments.of("toggle 0,0 through 999,999", new Answer("1000000", "2000000")),
            Arguments.of(
                "turn on 0,0 through 999,999\ntoggle 0,0 through 999,0",
                new Answer("999000", "1002000")
            ),
            Arguments.of(
                "turn on 0,0 through 999,999\nturn off 499,499 through 500,500",
                new Answer("999996", "999996")
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
            Arguments.of("turn on 0,0"), // Missing "through"
            Arguments.of("turn on 0,0 to 999,999"), // Wrong keyword (should be "through")
            Arguments.of("on 0,0 through 999,999"), // Missing "turn"
            Arguments.of("turn 0,0 through 999,999"), // Missing "on/off"
            Arguments.of("switch on 0,0 through 999,999"), // Wrong command
            Arguments.of("turn on 0 0 through 999 999"), // Missing commas
            Arguments.of("turn on 0,0 through 999"), // Incomplete coordinates
            Arguments.of("turn on a,b through c,d"), // Non-numeric coordinates
            Arguments.of("turn on 0,0 through 1000,1000"), // Coordinates too large (4 digits)
            Arguments.of("turn on 0,0 through 999,999\nabc"), // Invalid line in multi-line
            Arguments.of("toggle"), // Incomplete command
            Arguments.of("turn off"), // Incomplete command
            Arguments.of("a".repeat(15001)) // Too long
        );
    }
}


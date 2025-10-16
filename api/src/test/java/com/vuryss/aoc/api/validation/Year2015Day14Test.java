package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day14Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 14;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
                Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
                """,
                new Answer("2660", "1564")
            ),
            Arguments.of(
                """
                Rudolph can fly 22 km/s for 8 seconds, but then must rest for 165 seconds.
                Cupid can fly 8 km/s for 17 seconds, but then must rest for 114 seconds.
                Prancer can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
                Donner can fly 25 km/s for 6 seconds, but then must rest for 145 seconds.
                Dasher can fly 11 km/s for 12 seconds, but then must rest for 125 seconds.
                Comet can fly 21 km/s for 6 seconds, but then must rest for 121 seconds.
                Blitzen can fly 18 km/s for 3 seconds, but then must rest for 50 seconds.
                Vixen can fly 20 km/s for 4 seconds, but then must rest for 75 seconds.
                Dancer can fly 7 km/s for 20 seconds, but then must rest for 119 seconds.
                """,
                new Answer("2696", "1084")
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
            Arguments.of("Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds"), // Missing period
            Arguments.of("Comet can run 14 km/s for 10 seconds, but then must rest for 127 seconds."), // Wrong verb
            Arguments.of("Comet can fly km/s for 10 seconds, but then must rest for 127 seconds."), // Missing speed
            Arguments.of("Comet can fly 14 for 10 seconds, but then must rest for 127 seconds."), // Missing km/s
            Arguments.of("Comet can fly 14 km/s for seconds, but then must rest for 127 seconds."), // Missing duration
            Arguments.of("Comet can fly 14 km/s for 10 seconds, but then must rest for seconds."), // Missing rest time
            Arguments.of("Comet can fly -5 km/s for 10 seconds, but then must rest for 127 seconds."), // Negative speed
            Arguments.of("a".repeat(10001)) // Too long
        );
    }
}


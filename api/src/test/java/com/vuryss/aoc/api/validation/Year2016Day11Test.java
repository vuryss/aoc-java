package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2016Day11Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2016;
    }

    @Override
    protected int getDay() {
        return 11;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                The first floor contains a polonium generator, a thulium generator, a thulium-compatible microchip, a promethium generator, a ruthenium generator, a ruthenium-compatible microchip, a cobalt generator, and a cobalt-compatible microchip.
                The second floor contains a polonium-compatible microchip and a promethium-compatible microchip.
                The third floor contains nothing relevant.
                The fourth floor contains nothing relevant.
                """,
                new Answer("47", "71")
            ),
            Arguments.of(
                """
                The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
                The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
                The third floor contains a thulium-compatible microchip.
                The fourth floor contains nothing relevant.
                """,
                new Answer("37", "61")
            ),
            Arguments.of(
                """
                The first floor contains a promethium generator and a promethium-compatible microchip.
                The second floor contains a cobalt generator, a curium generator, a ruthenium generator, and a plutonium generator.
                The third floor contains a cobalt-compatible microchip, a curium-compatible microchip, a ruthenium-compatible microchip, and a plutonium-compatible microchip.
                The fourth floor contains nothing relevant.
                """,
                new Answer("33", "57")
            )
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\n"),
            Arguments.of("Invalid input without floor descriptions."),
            Arguments.of("Just some random text that doesn't match the pattern."),
            Arguments.of("12345"), // Numbers only
            Arguments.of("Floors: 1, 2, 3, 4"), // Wrong format
            Arguments.of("The first floor contains nothing relevant.") // Too short, missing other floors
        );
    }
}

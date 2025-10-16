package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day15Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 15;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
                Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
                """,
                new Answer("62842880", "57600000")
            ),
            Arguments.of(
                """
                Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2
                Sprinkles: capacity -3, durability 3, flavor 0, texture 0, calories 9
                Candy: capacity -1, durability 0, flavor 4, texture 0, calories 1
                Chocolate: capacity 0, durability 0, flavor -2, texture 2, calories 8
                """,
                new Answer("222870", "117936")
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
            Arguments.of("Butterscotch capacity -1, durability -2, flavor 6, texture 3, calories 8"), // Missing colon
            Arguments.of("Butterscotch: capacity, durability -2, flavor 6, texture 3, calories 8"), // Missing value
            Arguments.of("Butterscotch: capacity -1, durability -2, flavor 6, texture 3"), // Missing calories
            Arguments.of("Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories"), // Missing calories value
            Arguments.of("Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories abc"), // Non-numeric value
            Arguments.of("Butterscotch: capacity -1 durability -2 flavor 6 texture 3 calories 8"), // Missing commas
            Arguments.of("a".repeat(10001)) // Too long
        );
    }
}


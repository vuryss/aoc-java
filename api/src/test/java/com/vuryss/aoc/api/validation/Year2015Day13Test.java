package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day13Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 13;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Alice would gain 54 happiness units by sitting next to Bob.
                Alice would lose 79 happiness units by sitting next to Carol.
                Alice would lose 2 happiness units by sitting next to David.
                Bob would gain 83 happiness units by sitting next to Alice.
                Bob would lose 7 happiness units by sitting next to Carol.
                Bob would lose 63 happiness units by sitting next to David.
                Carol would lose 62 happiness units by sitting next to Alice.
                Carol would gain 60 happiness units by sitting next to Bob.
                Carol would gain 55 happiness units by sitting next to David.
                David would gain 46 happiness units by sitting next to Alice.
                David would lose 7 happiness units by sitting next to Bob.
                David would gain 41 happiness units by sitting next to Carol.
                """,
                new Answer("330", "286")
            ),
            Arguments.of(
                """
                Alice would lose 57 happiness units by sitting next to Bob.
                Alice would lose 62 happiness units by sitting next to Carol.
                Alice would lose 75 happiness units by sitting next to David.
                Alice would gain 71 happiness units by sitting next to Eric.
                Alice would lose 22 happiness units by sitting next to Frank.
                Alice would lose 23 happiness units by sitting next to George.
                Alice would lose 76 happiness units by sitting next to Mallory.
                Bob would lose 14 happiness units by sitting next to Alice.
                Bob would gain 48 happiness units by sitting next to Carol.
                Bob would gain 89 happiness units by sitting next to David.
                Bob would gain 86 happiness units by sitting next to Eric.
                Bob would lose 2 happiness units by sitting next to Frank.
                Bob would gain 27 happiness units by sitting next to George.
                Bob would gain 19 happiness units by sitting next to Mallory.
                Carol would gain 37 happiness units by sitting next to Alice.
                Carol would gain 45 happiness units by sitting next to Bob.
                Carol would gain 24 happiness units by sitting next to David.
                Carol would gain 5 happiness units by sitting next to Eric.
                Carol would lose 68 happiness units by sitting next to Frank.
                Carol would lose 25 happiness units by sitting next to George.
                Carol would gain 30 happiness units by sitting next to Mallory.
                David would lose 51 happiness units by sitting next to Alice.
                David would gain 34 happiness units by sitting next to Bob.
                David would gain 99 happiness units by sitting next to Carol.
                David would gain 91 happiness units by sitting next to Eric.
                David would lose 38 happiness units by sitting next to Frank.
                David would gain 60 happiness units by sitting next to George.
                David would lose 63 happiness units by sitting next to Mallory.
                Eric would gain 23 happiness units by sitting next to Alice.
                Eric would lose 69 happiness units by sitting next to Bob.
                Eric would lose 33 happiness units by sitting next to Carol.
                Eric would lose 47 happiness units by sitting next to David.
                Eric would gain 75 happiness units by sitting next to Frank.
                Eric would gain 82 happiness units by sitting next to George.
                Eric would gain 13 happiness units by sitting next to Mallory.
                Frank would gain 77 happiness units by sitting next to Alice.
                Frank would gain 27 happiness units by sitting next to Bob.
                Frank would lose 87 happiness units by sitting next to Carol.
                Frank would gain 74 happiness units by sitting next to David.
                Frank would lose 41 happiness units by sitting next to Eric.
                Frank would lose 99 happiness units by sitting next to George.
                Frank would gain 26 happiness units by sitting next to Mallory.
                George would lose 63 happiness units by sitting next to Alice.
                George would lose 51 happiness units by sitting next to Bob.
                George would lose 60 happiness units by sitting next to Carol.
                George would gain 30 happiness units by sitting next to David.
                George would lose 100 happiness units by sitting next to Eric.
                George would lose 63 happiness units by sitting next to Frank.
                George would gain 57 happiness units by sitting next to Mallory.
                Mallory would lose 71 happiness units by sitting next to Alice.
                Mallory would lose 28 happiness units by sitting next to Bob.
                Mallory would lose 10 happiness units by sitting next to Carol.
                Mallory would gain 44 happiness units by sitting next to David.
                Mallory would gain 22 happiness units by sitting next to Eric.
                Mallory would gain 79 happiness units by sitting next to Frank.
                Mallory would lose 16 happiness units by sitting next to George.
                """,
                new Answer("618", "601")
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
            Arguments.of("Alice would gain 54 happiness units by sitting next to Bob"), // Missing period
            Arguments.of("Alice would win 54 happiness units by sitting next to Bob."), // Wrong verb
            Arguments.of("Alice would gain happiness units by sitting next to Bob."), // Missing number
            Arguments.of("Alice would gain -5 happiness units by sitting next to Bob."), // Negative number
            Arguments.of("Alice would gain 54 by sitting next to Bob."), // Missing "happiness units"
            Arguments.of("Alice would gain 54 happiness units sitting next to Bob."), // Missing "by"
            Arguments.of("a".repeat(10001)) // Too long
        );
    }
}


package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class Year2015Day1Test {
    static Stream<Arguments> validInput() {
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

    @ParameterizedTest
    @MethodSource("validInput")
    void testValidInputWorks(String input, Answer answer) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/2015/1")
        .then()
            .statusCode(equalTo(200))
            .body("part1", equalTo(answer.part1))
            .body("part2", equalTo(answer.part2));
    }

    static Stream<Arguments> invalidInput() {
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

    @ParameterizedTest
    @MethodSource("invalidInput")
    void testInvalidInputFailsValidation(String input) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/2015/1")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("instance", equalTo("/solve/2015/1"))
            .body("violations", hasSize(equalTo(1)))
            .body("violations", hasItem("Invalid input. Please check the documentation for the puzzle."));
    }
}


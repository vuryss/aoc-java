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
class Year2015Day2Test {
    static Stream<Arguments> validInput() {
        return Stream.of(
            Arguments.of("2x3x4\n1x1x10", new Answer("101", "48")),
            Arguments.of("2x3x4", new Answer("58", "34")),
            Arguments.of("1x1x10", new Answer("43", "14"))
        );
    }

    @ParameterizedTest(name = "[{index}] Valid input")
    @MethodSource("validInput")
    void testValidInputWorks(String input, Answer answer) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/2015/2")
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
            Arguments.of("2x3x4\n1x1x10\nabc"),
            Arguments.of("2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x-3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10\n2x3x4\n1x1x10")
        );
    }

    @ParameterizedTest(name = "[{index}] Invalid input")
    @MethodSource("invalidInput")
    void testInvalidInputFailsValidation(String input) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/2015/2")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("instance", equalTo("/solve/2015/2"))
            .body("violations", hasSize(equalTo(1)))
            .body("violations", hasItem("Invalid input. Please check the documentation for the puzzle."));
    }
}


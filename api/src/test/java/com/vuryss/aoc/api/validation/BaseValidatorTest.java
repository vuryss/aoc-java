package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public abstract class BaseValidatorTest {

    protected abstract int getYear();
    protected abstract int getDay();

    @ParameterizedTest(name = "[{index}] Valid input")
    @MethodSource("validInputData")
    void testValidInputWorks(String input, Answer answer) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/" + getYear() + "/" + getDay())
        .then()
            .statusCode(equalTo(200))
            .body("part1", equalTo(answer.part1))
            .body("part2", equalTo(answer.part2));
    }

    @ParameterizedTest(name = "[{index}] Invalid input")
    @MethodSource("invalidInputData")
    void testInvalidInputFailsValidation(String input) {
        given()
            .contentType("text/plain")
            .body(input)
        .when()
            .post("/solve/" + getYear() + "/" + getDay())
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("instance", equalTo("/solve/" + getYear() + "/" + getDay()))
            .body("violations", hasSize(equalTo(1)))
            .body("violations", hasItem("Invalid input. Please check the documentation for the puzzle."));
    }
}


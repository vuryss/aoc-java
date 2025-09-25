package com.vuryss.aoc.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class SolveResourceTest {
    @Test
    public void testSolveEndpoint() {
        given()
            .contentType("text/plain")
            .body("(()(()())))))((((((((((((")
        .when()
            .post("/solve/2015/1")
        .then()
            .statusCode(200)
            .header("Content-Type", "application/json;charset=UTF-8")
            .body("part1", equalTo("9"))
            .body("part2", equalTo("11"));
    }
}

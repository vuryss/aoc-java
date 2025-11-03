package com.vuryss.aoc.api.validation;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class DayRangeValidationIntegrationTest {

    @Test
    void testHistoricalYearWithValidDay() {
        given()
            .contentType(ContentType.TEXT)
            .body("test input")
        .when()
            .post("/solve/2024/25")
        .then()
            .statusCode(equalTo(400))
            .body(not(containsString("Invalid day")))
            .body(not(containsString("Maximum day")));
    }

    @Test
    void testHistoricalYearWithInvalidDay() {
        given()
            .contentType(ContentType.TEXT)
            .body("test input")
        .when()
            .post("/solve/2024/26")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("violations", hasSize(equalTo(1)))
            .body("violations[0]", containsString("Invalid day 26 for year 2024"))
            .body("violations[0]", containsString("Maximum day for year 2024 is 25"));
    }

    @Test
    void test2025YearWithValidDay() {
        given()
            .contentType(ContentType.TEXT)
            .body("test input")
        .when()
            .post("/solve/2025/12")
        .then()
            .statusCode(equalTo(400))
            .body(not(containsString("Invalid day")))
            .body(not(containsString("Maximum day")));
    }

    @Test
    void test2025YearWithInvalidDay() {
        given()
            .contentType(ContentType.TEXT)
            .body("test input")
        .when()
            .post("/solve/2025/13")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("violations", hasSize(equalTo(1)))
            .body("violations[0]", containsString("Invalid day 13 for year 2025"))
            .body("violations[0]", containsString("Maximum day for year 2025 is 12"));
    }

    @Test
    void testUnsupportedYear() {
        given()
            .contentType(ContentType.TEXT)
            .body("test input")
        .when()
            .post("/solve/2014/1")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation failed"))
            .body("violations", hasSize(greaterThanOrEqualTo(1)))
            .body("violations", hasItem(containsString("Invalid day 1 for year 2014")))
            .body("violations", hasItem(containsString("Maximum day for year 2014 is not configured")));
    }
}

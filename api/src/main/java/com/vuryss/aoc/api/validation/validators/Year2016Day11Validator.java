package com.vuryss.aoc.api.validation.validators;

public class Year2016Day11Validator extends BaseValidator {
    public Year2016Day11Validator() {
        minLength(100);
        maxLength(3000);
        // Basic validation - ensure we have the required floor structure
        pattern("(?s).*first floor.*contains.*second floor.*contains.*third floor.*contains.*fourth floor.*contains.*");
    }
}
package com.vuryss.aoc.api.validation.validators;

public class Year2015Day1Validator extends BaseValidator {
    public Year2015Day1Validator() {
        minLength(1);
        maxLength(10_000);
        pattern("^[\\(\\)]*$");
    }
}


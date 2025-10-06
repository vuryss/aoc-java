package com.vuryss.aoc.api.validation.validators;

public class Year2015Day3Validator extends BaseValidator {
    public Year2015Day3Validator() {
        minLength(1);
        maxLength(10_000);
        pattern("^[\\^v<>,]+$");
    }
}


package com.vuryss.aoc.api.validation.validators;

public class Year2015Day4Validator extends BaseValidator {
    public Year2015Day4Validator() {
        minLength(1);
        maxLength(10);
        pattern("^[a-zA-Z0-9]+$");
    }
}


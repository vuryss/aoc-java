package com.vuryss.aoc.api.validation.validators;

public class Year2015Day20Validator extends BaseValidator {
    public Year2015Day20Validator() {
        minLength(1);
        maxLength(10);
        pattern("^\\d+$");
    }
}


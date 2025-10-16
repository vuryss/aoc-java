package com.vuryss.aoc.api.validation.validators;

public class Year2015Day8Validator extends BaseValidator {
    public Year2015Day8Validator() {
        minLength(2);
        maxLength(10_000);
        pattern("^(\"[0-9a-z\\\\\"x]*\"\n)*(\"[0-9a-z\\\\\"x]*\")\n?$");
    }
}


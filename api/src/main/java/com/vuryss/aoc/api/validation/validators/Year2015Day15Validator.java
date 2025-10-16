package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day15Validator extends BaseValidator {
    public Year2015Day15Validator() {
        minLength(40);
        maxLength(10_000);
        pattern("^[A-Za-z\\s:,\\-\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^[A-Za-z]+: capacity -?\\d+, durability -?\\d+, flavor -?\\d+, texture -?\\d+, calories -?\\d+$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}


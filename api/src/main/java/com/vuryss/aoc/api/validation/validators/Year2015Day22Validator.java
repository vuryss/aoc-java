package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day22Validator extends BaseValidator {
    public Year2015Day22Validator() {
        minLength(15);
        maxLength(100);
        pattern("^[A-Za-z\\s:\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var lines = input.trim().split("\n");

        if (lines.length != 2) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }

        if (!lines[0].matches("^Hit Points: \\d+$") ||
            !lines[1].matches("^Damage: \\d+$")) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }
    }
}


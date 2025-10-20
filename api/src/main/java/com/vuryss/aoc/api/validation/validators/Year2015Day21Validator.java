package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day21Validator extends BaseValidator {
    public Year2015Day21Validator() {
        minLength(20);
        maxLength(200);
        pattern("^[A-Za-z\\s:\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var lines = input.trim().split("\n");

        if (lines.length != 3) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }

        if (!lines[0].matches("^Hit Points: \\d+$") ||
            !lines[1].matches("^Damage: \\d+$") ||
            !lines[2].matches("^Armor: \\d+$")) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }
    }
}


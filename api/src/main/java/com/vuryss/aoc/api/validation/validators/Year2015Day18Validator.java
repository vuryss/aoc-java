package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day18Validator extends BaseValidator {
    public Year2015Day18Validator() {
        minLength(10);
        maxLength(15_000);
        pattern("^[#\\.\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var lines = input.trim().split("\n");
        var firstLineLength = lines[0].length();

        for (var line : lines) {
            if (line.length() != firstLineLength) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}


package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day19Validator extends BaseValidator {
    public Year2015Day19Validator() {
        minLength(10);
        maxLength(10_000);
        pattern("^[A-Za-z\\s=>\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var parts = input.trim().split("\n\n");

        if (parts.length != 2) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }

        var replacementRegex = "^[A-Za-z]+ => [A-Za-z]+$";

        for (var line : parts[0].split("\n")) {
            if (!line.matches(replacementRegex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }

        if (!parts[1].matches("^[A-Za-z]*$")) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }
    }
}


package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day23Validator extends BaseValidator {
    public Year2015Day23Validator() {
        minLength(5);
        maxLength(5_000);
        pattern("^[a-z\\s,+\\-\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^(hlf [ab]|tpl [ab]|inc [ab]|jmp [+\\-]\\d+|jie [ab], [+\\-]\\d+|jio [ab], [+\\-]\\d+)$";

        for (var line : input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}


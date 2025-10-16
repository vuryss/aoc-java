package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Year2015Day12Validator extends BaseValidator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Year2015Day12Validator() {
        minLength(2);
        maxLength(100_000);
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        try {
            objectMapper.readTree(input.trim());
        } catch (Exception e) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }
    }
}


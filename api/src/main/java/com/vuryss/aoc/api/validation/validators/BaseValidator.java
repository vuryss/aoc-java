package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.InputValidator;
import com.vuryss.aoc.api.validation.ValidationException;

public abstract class BaseValidator implements InputValidator {
    Integer minLength = null;
    Integer maxLength = null;
    String pattern = null;

    public void validate(String input) throws ValidationException {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException("Input cannot be empty");
        }

        if (minLength != null && input.length() < minLength) {
            throw new ValidationException("Input must be at least " + minLength + " characters");
        }

        if (maxLength != null && input.length() > maxLength) {
            throw new ValidationException("Input cannot exceed " + maxLength + " characters");
        }

        if (pattern != null && !input.matches(pattern)) {
            throw new ValidationException("Input does not match required pattern");
        }

        manualValidation();
    }

    protected void minLength(int minLength) {
        this.minLength = minLength;
    }

    protected void maxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    protected void pattern(String pattern) {
        this.pattern = pattern;
    }

    protected void manualValidation() {
    }
}


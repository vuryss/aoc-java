package com.vuryss.aoc.api.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of input validation.
 */
public record ValidationResult(boolean valid, List<String> errors) {

    public static ValidationResult success() {
        return new ValidationResult(true, List.of());
    }

    public static ValidationResult failure(String error) {
        return new ValidationResult(false, List.of(error));
    }

    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, new ArrayList<>(errors));
    }

    public String getErrorMessage() {
        return String.join("; ", errors);
    }
}


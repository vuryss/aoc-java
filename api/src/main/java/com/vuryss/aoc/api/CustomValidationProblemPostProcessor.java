package com.vuryss.aoc.api;

import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkiverse.resteasy.problem.postprocessing.ProblemContext;
import io.quarkiverse.resteasy.problem.postprocessing.ProblemPostProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;

@ApplicationScoped
public class CustomValidationProblemPostProcessor implements ProblemPostProcessor {
    @Override
    public HttpProblem apply(HttpProblem problem, ProblemContext context) {
        if (context.cause instanceof ConstraintViolationException validationException) {
            var violations = new ArrayList<String>();

            for (var violation: validationException.getConstraintViolations()) {
                violations.add(violation.getMessage());
            }

            return HttpProblem.builder()
                .withStatus(400)
                .withTitle("Validation failed")
                .withInstance(problem.getInstance())
                .with("violations", violations)
                .build();
        }

        return problem;
    }
}

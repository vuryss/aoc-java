package com.vuryss.aoc.api;

import com.vuryss.aoc.api.process.ProcessExecutionException;
import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkiverse.resteasy.problem.postprocessing.PostProcessorsRegistry;
import io.quarkiverse.resteasy.problem.postprocessing.ProblemContext;
import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
@Priority(Priorities.USER)
public class ProcessExecutionExceptionMapper implements ExceptionMapper<ProcessExecutionException> {
    public static final PostProcessorsRegistry postProcessorsRegistry = new PostProcessorsRegistry();

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ProcessExecutionException exception) {
        Log.info("Process execution failed: " + exception.getMessage());

        HttpProblem problem = HttpProblem.builder()
            .withStatus(400)
            .withTitle("Execution failed")
            .build();

        ProblemContext context = ProblemContext.of(exception, uriInfo);
        HttpProblem finalProblem = postProcessorsRegistry.applyPostProcessing(problem, context);

        return finalProblem.toResponse();
    }
}
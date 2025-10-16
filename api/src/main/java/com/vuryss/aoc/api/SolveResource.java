package com.vuryss.aoc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.api.process.ProcessExecutor;
import com.vuryss.aoc.api.validation.AocInputValid;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.Range;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import java.time.Duration;

@Path("/solve")
public class SolveResource {
    private final SolverCommandBuilder solverCommandBuilder;

    public SolveResource(
        SolverCommandBuilder solverCommandBuilder
    ) {
        this.solverCommandBuilder = solverCommandBuilder;
    }

    @Path("/test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Hello World";
    }

    @Path("/{year:20[12][0-9]}/{day:[1-9]|1[0-9]|2[0-5]}")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @AocInputValid
    public Uni<SolutionResult> solve(
        @RestPath @NotNull @Range(min = 2015, max = 2025) Integer year,
        @RestPath @NotNull @Range(min = 1, max = 25) Integer day,
        @NotNull String input
    ) {
        var command = solverCommandBuilder.build(year, day, input);
        var executor = new ProcessExecutor();

        Log.info("Command: " + String.join(" ", command));

        return Uni.createFrom().completionStage(() -> executor
            .execute(command, Duration.ofSeconds(5))
            .thenApply(processResult -> {
                if (processResult.exitCode() != 0) {
                    if (3 == processResult.exitCode()) {
                        Log.info("Process existed with code 3 - memory limit exceeded");
                    } else {
                        Log.info("Process execution failed with code " + processResult.exitCode());
                        Log.info("Stdout: " + processResult.stdout());
                        Log.info("Stderr: " + processResult.stderr());
                    }

                    throw new BadRequestException(
                        "Cannot execute solution, please verify your input or contact the administrator."
                    );
                }

                var commandOutput = processResult.stdout();

                if (!commandOutput.startsWith("{")) {
                    throw new RuntimeException("Invalid output: " + commandOutput);
                }

                JsonNode result;

                try {
                    result = new ObjectMapper().readTree(processResult.stdout());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                return new SolutionResult(
                    result.path("part1").asText(),
                    result.path("part2").asText()
                );
            }));
    }
}

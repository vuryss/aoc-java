package com.vuryss.aoc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuryss.aoc.api.process.ProcessExecutor;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestPath;
import java.time.Duration;
import java.util.concurrent.*;

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
    public Uni<SolutionResult> solve(@RestPath Integer year, @RestPath Integer day, String input) {
        var command = solverCommandBuilder.build(year, day, input);
        var executor = new ProcessExecutor();

        return Uni.createFrom().completionStage(() -> executor
            .execute(command, Duration.ofSeconds(5))
            .thenApply(processResult -> {
                if (processResult.exitCode() != 0) {
                    return new SolutionResult(
                        SolutionResult.Status.ERROR,
                        processResult.stderr()
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

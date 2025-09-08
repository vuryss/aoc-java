package com.vuryss.aoc.cli;

import io.quarkus.picocli.runtime.PicocliCommandLineFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import picocli.CommandLine;

@ApplicationScoped
public class CustomCommandLineConfiguration {
    @Produces
    CommandLine customCommandLine(PicocliCommandLineFactory factory) {
        return factory
            .create()
            .setExecutionExceptionHandler(new PrintExceptionMessageHandler());
    }
}

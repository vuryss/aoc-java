package com.vuryss.aoc.cli;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

public class PrintExceptionMessageHandler implements CommandLine.IExecutionExceptionHandler {
    @Override
    public int handleExecutionException(Exception ex,
                                        CommandLine commandLine,
                                        CommandLine.ParseResult fullParseResult) throws Exception {
        if (ex instanceof CliException) {
            if (isJsonOutput(commandLine)) {
                System.out.println(
                    new ObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(
                            new Result(Result.Status.ERROR, ex.getMessage())
                        )
                );

                return 1;
            }

            commandLine.getErr().println(
                commandLine.getColorScheme().errorText(ex.getMessage())
            );

            return 1;
        }

        throw ex;
    }

    public boolean isJsonOutput(CommandLine commandLine) {
        if (commandLine.getCommandName().equals("solve")) {
            var command = (SolveCommand) commandLine.getCommand();

            return command.jsonOutput;
        }

        return false;
    }
}

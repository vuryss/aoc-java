package com.vuryss.aoc.cli;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(
    name = "main",
    mixinStandardHelpOptions = true,
    description = "Advent of Code CLI",
    subcommands = {
        SolveCommand.class,
        ScaffoldCommand.class
    }
)
public class MainCommand {
}
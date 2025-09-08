package com.vuryss.aoc.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class SolverCommandBuilder {
    String cliJarPath;

    SolverCommandBuilder(
        @ConfigProperty(name = "aoc.cli.jar.path") String cliJarPath
    ) {
        this.cliJarPath = cliJarPath;
    }

    public List<String> build(int year, int day, String input) {
        var heredoc = "__AOC_HEREDOC_EOF__";
        var shellCmd = new StringBuilder()
            .append("cat <<'" + heredoc + "' | java -Dquarkus.log.console.enable=false -Xmx256m -jar ")
            .append(getCliJarPath())
            .append(" solve --year ")
            .append(year)
            .append(" --day ")
            .append(day)
            .append(" -j")
            .append(" --input-from-stdin\n")
            .append(input == null ? "" : input)
            .append(input != null && !input.endsWith("\n") ? "\n" : "")
            .append(heredoc);

        return java.util.List.of("bash", "-lc", shellCmd.toString());
    }

    public Path getCliJarPath() {
        java.nio.file.Path jar = java.nio.file.Path.of(cliJarPath);

        if (!Files.isRegularFile(jar)) {
            throw new InternalServerErrorException("CLI jar not found at configured path: " + jar);
        }

        return jar;
    }
}

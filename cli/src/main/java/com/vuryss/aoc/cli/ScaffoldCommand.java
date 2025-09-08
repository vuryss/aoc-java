package com.vuryss.aoc.cli;

import com.vuryss.aoc.solutions.SolutionScaffolding;
import picocli.CommandLine;

@CommandLine.Command(name = "scaffold", description = "Scaffold a new solution class for a given year and day")
public class ScaffoldCommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Year of the challenge")
    int year;

    @CommandLine.Parameters(index = "1", description = "Day of the challenge")
    int day;

    private final SolutionScaffolding solutionScaffolding;

    public ScaffoldCommand(
        SolutionScaffolding solutionScaffolding
    ) {
        this.solutionScaffolding = solutionScaffolding;
    }

    @Override
    public void run() {
        System.out.println("Scaffold year " + year + " day " + day);

        solutionScaffolding.scaffold(year, day);
    }
}
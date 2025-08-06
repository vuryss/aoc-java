package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.*;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var graph = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        for (var line: lines) {
            Regex.matchAll("\\w+", line).stream().filter(s -> !graph.containsVertex(s)).forEach(graph::addVertex);
        }

        for (var line: lines) {
            var connections = Regex.matchAll("\\w+", line);
            var name = connections.removeFirst();

            connections.stream().filter(s -> !graph.containsEdge(name, s)).forEach(s -> graph.addEdge(name, s));
        }

        var stoerWagnerMinimumCut = new StoerWagnerMinimumCut<>(graph);
        var minimumCutSize = stoerWagnerMinimumCut.minCut().size();

        return String.valueOf(minimumCutSize * (graph.vertexSet().size() - minimumCutSize));
    }

    @Override
    public String part2Solution(String input) {
        return "Merry Christmas!";
    }
}

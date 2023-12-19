package com.vuryss.aoc.solutions.event2023;

import com.google.common.collect.Range;
import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;

@SuppressWarnings("unused")
public class Day19 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
            
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """,
            "19114"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
            
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """,
            "167409079868000"
        );
    }

    @Override
    public String part1Solution(String input) {
        var inputParts = input.trim().split("\n\n");
        var workflows = parseWorkflows(inputParts[0]);
        var partsList = parseParts(inputParts[1]);
        var acceptedParts = new ArrayList<HashMap<Character, Integer>>();
        var sum = 0L;

        for (var part: partsList) {
            var workflow = workflows.get("in");

            nextWorkflow:
            while (true) {
                for (var condition: workflow.conditions) {
                    var registerValue = part.get(condition.register);

                    switch (condition.operation) {
                        case '<' -> {
                            if (registerValue < condition.value) {
                                if (condition.targetWorkflow.equals("A")) {
                                    acceptedParts.add(part);
                                    break nextWorkflow;
                                }

                                if (condition.targetWorkflow.equals("R")) {
                                    break nextWorkflow;
                                }

                                workflow = workflows.get(condition.targetWorkflow);
                                continue nextWorkflow;
                            }
                        }
                        case '>' -> {
                            if (registerValue > condition.value) {
                                if (condition.targetWorkflow.equals("A")) {
                                    acceptedParts.add(part);
                                    break nextWorkflow;
                                }

                                if (condition.targetWorkflow.equals("R")) {
                                    break nextWorkflow;
                                }

                                workflow = workflows.get(condition.targetWorkflow);
                                continue nextWorkflow;
                            }
                        }
                    }
                }

                if (workflow.targetWorkflow.equals("A")) {
                    acceptedParts.add(part);
                    break;
                }

                if (workflow.targetWorkflow.equals("R")) {
                    break;
                }

                workflow = workflows.get(workflow.targetWorkflow);
            }
        }

        for (var part: acceptedParts) {
            for (var es: part.entrySet()) {
                sum += es.getValue();
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var inputParts = input.trim().split("\n\n");
        var sum = 0L;
        var workflows = parseWorkflows(inputParts[0]);
        var workflowQueue = new HashMap<String, ArrayList<HashMap<Character, Range<Integer>>>>();
        var acceptedRanges = new ArrayList<HashMap<Character, Range<Integer>>>();
        boolean hasUnprocessedRanges = true;

        for (var workflow: workflows.values()) {
            workflowQueue.put(workflow.name, new ArrayList<>());
        }

        var partRanges = new ArrayList<HashMap<Character, Range<Integer>>>() {{
            add(new HashMap<>() {{
                put('x', Range.closed(1, 4000));
                put('m', Range.closed(1, 4000));
                put('a', Range.closed(1, 4000));
                put('s', Range.closed(1, 4000));
            }});
        }};

        workflowQueue.get("in").addAll(partRanges);

        while (hasUnprocessedRanges) {
            hasUnprocessedRanges = false;

            for (var work: workflowQueue.entrySet()) {
                if (work.getValue().isEmpty()) {
                    continue;
                }

                hasUnprocessedRanges = true;

                var workflow = workflows.get(work.getKey());
                var ranges = work.getValue();
                workflowQueue.put(work.getKey(), new ArrayList<>());

                nextRange:
                for (var range: ranges) {
                    for (var condition: workflow.conditions) {
                        var registerRange = range.get(condition.register);
                        Range<Integer> conditionRange;
                        Range<Integer> remainingRange;

                        if (condition.operation == '<') {
                            conditionRange = Range.closed(1, condition.value - 1);
                            remainingRange = Range.closed(condition.value, 4000);
                        } else {
                            conditionRange = Range.closed(condition.value + 1, 4000);
                            remainingRange = Range.closed(1, condition.value);
                        }

                        if (!registerRange.isConnected(conditionRange)) {
                            continue;
                        }

                        var overlapping = registerRange.intersection(conditionRange);
                        var newRange = new HashMap<>(range);
                        newRange.put(condition.register, overlapping);

                        if (condition.targetWorkflow.equals("A")) {
                            acceptedRanges.add(newRange);
                        } else if (!condition.targetWorkflow.equals("R")) {
                            workflowQueue.get(condition.targetWorkflow).add(newRange);
                        }

                        if (remainingRange.isConnected(registerRange)) {
                            range.put(condition.register, remainingRange.intersection(registerRange));
                        } else {
                            continue nextRange;
                        }
                    }

                    if (workflow.targetWorkflow.equals("A")) {
                        acceptedRanges.add(range);
                    } else if (!workflow.targetWorkflow.equals("R")) {
                        workflowQueue.get(workflow.targetWorkflow).add(range);
                    }
                }
            }
        }

        for (var acceptedRange: acceptedRanges) {
            var rangeProduct = 1L;
            for (var es: acceptedRange.entrySet()) {
                var range = es.getValue();
                rangeProduct *= range.upperEndpoint() - range.lowerEndpoint() + 1;
            }

            sum += rangeProduct;
        }

        return String.valueOf(sum);
    }

    private HashMap<String, Workflow> parseWorkflows(String input) {
        var workflows = new HashMap<String, Workflow>();
        var workflowsLines = input.split("\n");

        for (var workflowLine: workflowsLines) {
            var conditions = new LinkedList<Condition>();
            var workflowHeaders = Regex.matchGroups("(\\w+)\\{(.*)\\}", workflowLine);
            assert workflowHeaders != null;
            var workflowParts = workflowHeaders.get(1).split(",");

            for (var i = 0; i < workflowParts.length - 1; i++) {
                var c = Regex.matchGroups("(\\w+)([^\\w])([0-9]+):(\\w+)", workflowParts[i]);
                assert c != null;
                conditions.add(new Condition(c.get(0).charAt(0), c.get(1).charAt(0), Integer.parseInt(c.get(2)), c.get(3)));
            }

            workflows.put(
                workflowHeaders.get(0),
                new Workflow(workflowHeaders.get(0), conditions, workflowParts[workflowParts.length - 1])
            );
        }

        return workflows;
    }

    private ArrayList<HashMap<Character, Integer>> parseParts(String input) {
        var parts = new ArrayList<HashMap<Character, Integer>>();
        var partsLines = input.split("\n");

        for (var partLine: partsLines) {
            var partRegisters = partLine.substring(1, partLine.length() - 1).split(",");
            var part = new HashMap<Character, Integer>();

            for (var partRegister: partRegisters) {
                var registerParts = partRegister.split("=");
                part.put(registerParts[0].charAt(0), Integer.parseInt(registerParts[1]));
            }

            parts.add(part);
        }

        return parts;
    }

    record Condition(Character register, Character operation, Integer value, String targetWorkflow) {}
    record Workflow(String name, List<Condition> conditions, String targetWorkflow) {}
}

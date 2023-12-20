package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.MathUtil;
import com.vuryss.aoc.util.Regex;

import java.util.*;

//@SuppressWarnings("unused")
public class Day20 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """,
            "32000000",
            """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
            """,
            "11687500"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            """,
            ""
        );
    }

    public static HashMap<String, Long> tracker = new HashMap<>();

    @Override
    public String part1Solution(String input) {
        var modules = parseModules(input);
        var sumLow = 0L;
        var sumHigh = 0L;

        for (var i = 0; i < 1000; i++) {
            modules.get("button").process(i);
            boolean allProcessed = false;

            while (!allProcessed) {
                allProcessed = true;

                for (var module : modules.values()) {
                    module.loadQueueToInputs();
                }

                for (var module : modules.values()) {
                    if (!module.name.equals("button")) {
                        allProcessed = !module.process(i) && allProcessed;
                    }
                }
            }
        }

        for (var module: modules.entrySet()) {
            sumLow += module.getValue().sentLowPulses;
            sumHigh += module.getValue().sentHighPulses;
        }

        return String.valueOf(sumLow * sumHigh);
    }

    @Override
    public String part2Solution(String input) {
        var modules = parseModules(input);
        var rxInputs = modules.get("rx").inputs.keySet();
        var trackedConjunctions = new ArrayList<Module>();
        var counter = 0L;

        for (var rxInput: rxInputs) {
            for (var inp: rxInput.inputs.keySet()) {
                inp.track = true;
                trackedConjunctions.add(inp);
            }
        }

        while (++counter > 0) {
            modules.get("button").process(counter);
            boolean allProcessed = false;

            while (!allProcessed) {
                allProcessed = true;

                for (var module : modules.values()) {
                    module.loadQueueToInputs();
                }

                for (var module : modules.values()) {
                    if (!module.name.equals("button")) {
                        allProcessed = !module.process(counter) && allProcessed;
                    }
                }
            }

            if (Day20.tracker.size() == trackedConjunctions.size()) {
                return String.valueOf(MathUtil.lcm(Day20.tracker.values()));
            }
        }

        return "";
    }

    private HashMap<String, Module> parseModules(String input) {
        var lines = input.trim().split("\n");
        var modules = new HashMap<String, Module>();

        for (var line: lines) {
            var m = Regex.matchGroups("^(%|&)?([a-z]+) -> (.+)", line);
            assert m != null;
            var name = m.size() == 2 ? m.get(0) : m.get(1);
            var type = m.size() == 2 ? null : m.get(0).charAt(0);
            var module = new Module(name, type);
            modules.put(module.name, module);
        }

        for (var line: lines) {
            var m = Regex.matchGroups("^(?:%|&)?([a-z]+) -> (.+)", line);
            assert m != null;
            var module = modules.get(m.getFirst());

            for (var output: m.getLast().split(", ")) {
                modules.putIfAbsent(output, new Module(output, null));
                module.outputs.add(modules.get(output));
                modules.get(output).inputs.put(module, false);
            }
        }

        var button = new Module("button", null);
        var broadcaster = modules.get("broadcaster");
        button.outputs.add(modules.get("broadcaster"));
        broadcaster.inputs.put(button, false);
        modules.put("button", button);

        return modules;
    }

    private static class Module {
        public long sentLowPulses = 0;
        public long sentHighPulses = 0;
        public final String name;
        public final Character type;
        public HashMap<Module, Boolean> inputs;
        public List<Module> outputs;
        public HashMap<Module, Boolean> inputPulses = new HashMap<>();
        public boolean state = false;
        public boolean track = false;
        public HashMap<Module, Boolean> queue = new HashMap<>();

        public Module(String name, Character type) {
            this.name = name;
            this.type = type;
            this.inputs = new HashMap<>();
            this.outputs = new ArrayList<>();
        }

        public void sendPulse(Module source, boolean pulse) {
            queue.put(source, pulse);
        }

        public void loadQueueToInputs() {
            inputPulses = new HashMap<>(queue);
            queue.clear();
        }

        public boolean process(long counter) {
            if (name.equals("button")) {
                for (var output: outputs) {
                    output.sendPulse(this, false);
                    sentLowPulses++;
                }

                return true;
            }

            if (inputPulses.isEmpty()) {
                return false;
            }

            else if (name.equals("broadcaster")) {
                for (var output : outputs) {
                    output.sendPulse(this, false);
                    sentLowPulses++;
                }
            } else if (type != null && type == '%') {
                for (var incomingPulse: inputPulses.entrySet()) {
                    if (!incomingPulse.getValue()) {
                        if (state) {
                            for (var output: outputs) {
                                output.sendPulse(this, false);
                                sentLowPulses++;
                            }
                        } else {
                            for (var output: outputs) {
                                output.sendPulse(this, true);
                                sentHighPulses++;
                            }
                        }
                        state = !state;
                    }
                }
            } else if (type != null && type == '&') {
                for (var incomingPulse: inputPulses.entrySet()) {
                    inputs.put(incomingPulse.getKey(), incomingPulse.getValue());

                    boolean hasLow = false;

                    for (var input: inputs.entrySet()) {
                        if (!input.getValue()) {
                            hasLow = true;
                            break;
                        }
                    }

                    if (hasLow) {
                        if (this.track) {
                            Day20.tracker.put(this.name, counter);
                        }

                        for (var output : outputs) {
                            output.sendPulse(this, true);
                            sentHighPulses++;
                        }
                    } else {
                        for (var output : outputs) {
                            output.sendPulse(this, false);
                            sentLowPulses++;
                        }
                    }
                }
            }

            inputPulses.clear();
            return true;
        }
    }
}
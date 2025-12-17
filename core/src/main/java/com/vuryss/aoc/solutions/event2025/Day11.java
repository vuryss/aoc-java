package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """,
            "2"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var devices = parseDevices(input);
        var dev = devices.get("you");
        dev.pathCount = 1L;
        var out = devices.get("out");

        return String.valueOf(out.getPathCount());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var devices = parseDevices(input);
        var srv = devices.get("svr");
        srv.pathCount = 1L;
        var out = devices.get("out");
        out.calculatePathCountThroughFftDac();

        return String.valueOf(out.passedThroughBoth);
    }

    private Map<String, Device> parseDevices(String input) {
        var lines = input.trim().split("\n");
        var devices = new HashMap<String, Device>();

        for (var line: lines) {
            var parts = line.split(": ");
            var device = devices.computeIfAbsent(parts[0], Device::new);
            var outputs = parts[1].split(" ");

            for (var output: outputs) {
                devices.computeIfAbsent(output, Device::new).inputs.add(device);
            }
        }

        return devices;
    }

    private static class Device {
        public String name;
        public Set<Device> inputs = new HashSet<>();
        public Long pathCount = null;
        public Long passedThroughDac = 0L;
        public Long passedThroughFft = 0L;
        public Long passedThroughBoth = null;

        public Device(String name) {
            this.name = name;
        }

        public Long getPathCount() {
            if (null == pathCount) {
                pathCount = inputs.stream().mapToLong(Device::getPathCount).sum();
            }

            return pathCount;
        }

        public void calculatePathCountThroughFftDac() {
            if (passedThroughBoth == null) {
                inputs.forEach(Device::calculatePathCountThroughFftDac);
            }

            var pathCount = getPathCount();
            passedThroughFft = inputs.stream().mapToLong(d -> d.passedThroughFft).sum();
            passedThroughDac = inputs.stream().mapToLong(d -> d.passedThroughDac).sum();
            passedThroughBoth = inputs.stream().mapToLong(d -> d.passedThroughBoth).sum();

            if (name.equals("fft")) {
                passedThroughFft = pathCount;
                passedThroughBoth = Math.min(passedThroughDac, pathCount);
            } else if (name.equals("dac")) {
                passedThroughDac = pathCount;
                passedThroughBoth = Math.min(passedThroughFft, pathCount);
            }
        }
    }
}

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
        dev.pathCount = 1;
        var out = devices.get("out");

        return String.valueOf(out.getPathCount());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var devices = parseDevices(input);

        // srv -> fft
        var start = devices.get("svr");
        start.pathCount = 1;
        var end = devices.get("fft");
        long a = end.getPathCount();

        // Reset path counts
        devices.values().forEach(dev -> dev.pathCount = null);

        // fft -> dac
        start = devices.get("fft");
        start.pathCount = 1;
        end = devices.get("dac");
        long b = end.getPathCount();

        // Reset path counts
        devices.values().forEach(dev -> dev.pathCount = null);

        // dac -> out
        start = devices.get("dac");
        start.pathCount = 1;
        end = devices.get("out");
        long c = end.getPathCount();

        return String.valueOf(a * b * c);
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
                device.outputs.add(devices.get(output));
            }
        }

        return devices;
    }

    private static class Device {
        public String name;
        public Set<Device> outputs = new HashSet<>();
        public Set<Device> inputs = new HashSet<>();
        public Integer pathCount = null;

        public Device(String name) {
            this.name = name;
        }

        public Integer getPathCount() {
            if (null == pathCount) {
                pathCount = inputs.stream().mapToInt(Device::getPathCount).sum();
            }

            return pathCount;
        }
    }
}

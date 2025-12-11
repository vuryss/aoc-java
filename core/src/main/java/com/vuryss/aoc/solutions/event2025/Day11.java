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
        var startDeviceName = "svr";
        var endDeviceName = "out";
        String firstStop = "fft", secondStop = "dac";

        // Check if there is a path from FFT to DAC first
        var start = devices.get(firstStop);
        start.pathCount = 1;
        var end = devices.get(secondStop);
        long pathBetweenMiddleStops = end.getPathCount();

        // If no path, try DAC to FFT
        if (pathBetweenMiddleStops == 0) {
            firstStop = "dac";
            secondStop = "fft";
            devices.values().forEach(dev -> dev.pathCount = null);
            start = devices.get(firstStop);
            start.pathCount = 1;
            end = devices.get(secondStop);
            pathBetweenMiddleStops = end.getPathCount();
        }

        // srv -> (fft or dac)
        devices.values().forEach(dev -> dev.pathCount = null);
        start = devices.get(startDeviceName);
        start.pathCount = 1;
        end = devices.get(firstStop);
        long startToFirstStop = end.getPathCount();

        // (fft or dac) to out
        devices.values().forEach(dev -> dev.pathCount = null);
        start = devices.get(secondStop);
        start.pathCount = 1;
        end = devices.get(endDeviceName);
        long secondStopToEnd = end.getPathCount();

        return String.valueOf(startToFirstStop * pathBetweenMiddleStops * secondStopToEnd);
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

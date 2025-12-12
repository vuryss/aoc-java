package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var sections = input.trim().split("\n\n");
        var regionInputs = sections[sections.length - 1].split("\n");
        var regions = new Region[regionInputs.length];

        for (var i = 0; i < regionInputs.length; i++) {
            var parts = regionInputs[i].split(": ");
            var dimensions = parts[0].split("x");
            var presents = StringUtil.ints(parts[1]);
            regions[i] = new Region(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]), presents.stream().mapToInt(Integer::intValue).toArray());
        }

        if (!isTest) {
            // TROLL ALERT, TROLL ALERT
            return Arrays.stream(regions).filter(this::canFit).count() + "";
        }

        char[][][] shapes = new char[sections.length - 1][3][3];

        for (var i = 0; i < shapes.length; i++) {
            var lines = sections[i].split("\n");
            for (var j = 0; j < 3; j++) {
                shapes[i][j] = lines[j + 1].toCharArray();
            }
        }

        var count = 0;

        for (var region: regions) {
            if (canFitTest(region, shapes)) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    private boolean canFit(Region region) {
        int shapeCount = 0;
        int maxShapes = region.height / 3 * region.width / 3;

        for (var i = 0; i < region.presents.length; i++) {
            shapeCount += region.presents[i];
        }

        return shapeCount <= maxShapes;
    }

    private boolean canFitTest(Region region, char[][][] shapes) {
        // TODO: Solve it for the test cases

        return false;
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    record Region(int width, int height, int[] presents) {}
}

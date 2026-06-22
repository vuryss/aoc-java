package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.LetterOCR;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    private final int rows = 6;
    private final int cols = 25;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var totalLayerPixels = cols * rows;
        var layersCount = input.trim().length() / totalLayerPixels;
        String[] layers = new String[layersCount];
        var minZeroes = Integer.MAX_VALUE;
        var layerIndex = -1;

        for (var i = 0; i < layersCount; i++) {
            layers[i] = input.substring(i * totalLayerPixels, (i + 1) * totalLayerPixels);
            var chars = StringUtil.tally(layers[i]);

            if (chars.getOrDefault('0', 0) < minZeroes) {
                minZeroes = chars.getOrDefault('0', 0);
                layerIndex = i;
            }
        }

        var chars = StringUtil.tally(layers[layerIndex]);

        return (chars.get('1') * chars.get('2')) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var totalLayerPixels = rows * cols;
        var layersCount = input.trim().length() / totalLayerPixels;
        int[][] image = new int[rows][cols];
        for (var row : image) Arrays.fill(row, 2);

        for (var i = 0; i < layersCount; i++) {
            var layer = input.substring(i * totalLayerPixels, (i + 1) * totalLayerPixels);
            var index = 0;

            for (var ch: layer.chars().toArray()) {
                int row = index / cols;
                int column = index % cols;
                image[row][column] = image[row][column] == 2 ? ch - '0' : image[row][column];
                index++;
            }
        }

        HashSet<Point> points = new HashSet<>();

        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                if (image[i][j] == 1) points.add(new Point(j, i));
            }
        }

        return LetterOCR.decode46(points);
    }
}

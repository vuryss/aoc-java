package com.vuryss.aoc.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LetterOCR {
    private static final String ALPHABET_4_6_CHARACTERS = "ABCEFGHIJKLOPRSUYZ";
    private static final String ALPHABET_4_6 = """
         ##  ###   ##  #### ####  ##  #  # ###    ## #  # #     ##  ###  ###   ### #  # #   # ####\s
        #  # #  # #  # #    #    #  # #  #  #      # # #  #    #  # #  # #  # #    #  # #   #    #\s
        #  # ###  #    ###  ###  #    ####  #      # ##   #    #  # #  # #  # #    #  #  # #    # \s
        #### #  # #    #    #    # ## #  #  #      # # #  #    #  # ###  ###   ##  #  #   #    #  \s
        #  # #  # #  # #    #    #  # #  #  #   #  # # #  #    #  # #    # #     # #  #   #   #   \s
        #  # ###   ##  #### #     ### #  # ###   ##  #  # ####  ##  #    #  # ###   ##    #   ####\s
        """;

    public static String decode46(HashSet<Point> grid) {
        var result = new StringBuilder();
        var startX = 0;
        var map46 = get46Map();

        while (true) {
            var points = new HashSet<Point>();

            for (var x = startX; x < startX + 4; x++) {
                for (var y = 0; y < 6; y++) {
                    if (grid.contains(new Point(x, y))) {
                        points.add(new Point(x - startX, y));
                    }
                }
            }

            if (points.isEmpty()) {
                break;
            }

            if (map46.containsKey(points)) {
                result.append(map46.get(points));
                startX += 5;
                continue;
            }

            throw new RuntimeException("Letter not found. Please check!");
        }

        return result.toString();
    }

    private static Map<Set<Point>, Character> get46Map() {
        var map = new HashMap<Set<Point>, Character>();
        var alphabetLines = ALPHABET_4_6.split("\n", -1);

        for (var i = 0; i < ALPHABET_4_6_CHARACTERS.length(); i++) {
            var c = ALPHABET_4_6_CHARACTERS.charAt(i);
            var points = new HashSet<Point>();
            var startX = i * 5;

            for (var x = startX; x < startX + 4; x++) {
                for (var y = 0; y < 6; y++) {
                    if (alphabetLines[y].charAt(x) == '#') {
                        points.add(new Point(x - startX, y));
                    }
                }
            }

            map.put(points, c);
        }

        return map;
    }
}

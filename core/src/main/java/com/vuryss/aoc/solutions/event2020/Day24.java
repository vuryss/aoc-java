package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.PointHex;
import com.vuryss.aoc.util.PointyTopHexDirection;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            sesenwnenenewseeswwswswwnenewsewsw
            neeenesenwnwwswnenewnwwsewnenwseswesw
            seswneswswsenwwnwse
            nwnwneseeswswnenewneswwnewseswneseene
            swweswneswnenwsewnwneneseenw
            eesenwseswswnenwswnwnwsewwnwsene
            sewnenenenesenwsewnenwwwse
            wenwwweseeeweswwwnwwe
            wsweesenenewnwwnwsenewsenwwsesesenwne
            neeswseenwwswnwswswnw
            nenwswwsewswnenenewsenwsenwnesesenew
            enewnwewneswsewnwswenweswnenwsenwsw
            sweneswneswneneenwnewenewwneswswnese
            swwesenesewenwneswnwwneseswwne
            enesenwswwswneneswsenwnewswseenwsese
            wnwnesenesenenwwnenwsewesewsesesew
            nenewswnwewswnenesenwnesewesw
            eneswnwswnwsenenwnwnwwseeswneewsenese
            neswnwewnwnwseenwseesewsenwsweewe
            wseweeenwnesenwwwswnew
            """,
            "10"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            sesenwnenenewseeswwswswwnenewsewsw
            neeenesenwnwwswnenewnwwsewnenwseswesw
            seswneswswsenwwnwse
            nwnwneseeswswnenewneswwnewseswneseene
            swweswneswnenwsewnwneneseenw
            eesenwseswswnenwswnwnwsewwnwsene
            sewnenenenesenwsewnenwwwse
            wenwwweseeeweswwwnwwe
            wsweesenenewnwwnwsenewsenwwsesesenwne
            neeswseenwwswnwswswnw
            nenwswwsewswnenenewsenwsenwnesesenew
            enewnwewneswsewnwswenweswnenwsenwsw
            sweneswneswneneenwnewenewwneswswnese
            swwesenesewenwneswnwwneseswwne
            enesenwswwswneneswsenwnewswseenwsese
            wnwnesenesenenwwnenwsewesewsesesew
            nenewswnwewswnenesenwnesewesw
            eneswnwswnwsenenwnwnwwseeswneewsenese
            neswnwewnwnwseenwseesewsenwsweewe
            wseweeenwnesenwwwswnew
            """,
            "2208"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return parseInitialState(input).size() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var blacks = parseInitialState(input);

        for (int i = 0; i < 100; i++) {
            var newBlacks = new HashSet<PointHex>();
            var neighbourCount = new HashMap<PointHex, Integer>();

            for (var black: blacks) {
                for (var adjacent: black.adjacent()) neighbourCount.merge(adjacent, 1, Integer::sum);
            }

            for (var es: neighbourCount.entrySet()) {
                var tile = es.getKey();
                var count = es.getValue();

                if (count == 2 || (count == 1 && blacks.contains(tile))) newBlacks.add(tile);
            }

            blacks = newBlacks;
        }

        return blacks.size() + "";
    }

    private Set<PointHex> parseInitialState(String input) {
        var lines = input.trim().split("\n");
        var blacks = new HashSet<PointHex>();
        String dir;
        PointHex point;
        int index;

        for (var line: lines) {
            index = 0;
            point = new PointHex(0, 0 ,0);

            while (index < line.length()) {
                if (line.charAt(index) == 'n' || line.charAt(index) == 's') {
                    dir = line.substring(index, index + 2);
                    index += 2;
                } else {
                    dir = String.valueOf(line.charAt(index));
                    index++;
                }

                point = point.goInDirection(PointyTopHexDirection.from(dir));
            }

            if (blacks.contains(point)) blacks.remove(point);
            else blacks.add(point);
        }

        return blacks;
    }
}

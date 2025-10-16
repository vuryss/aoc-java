package com.vuryss.aoc.solutions.event2021;

import com.google.common.primitives.Chars;
import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.*;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
            """,
            "0",
            """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
            """,
            "26"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
            """,
            "5353",
            """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
            """,
            "61229"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var count = 0;

        for (var line: lines) {
            var parts = line.split(" \\| ");
            var outputValues = parts[1].split(" ");

            for (var value: outputValues) {
                var strLength = value.length();

                if (strLength == 2 || strLength == 3 || strLength == 4 || strLength == 7) {
                    count++;
                }
            }

        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.split("\n");
        var sum = 0;

        for (var line: lines) {
            var parts = line.split(" \\| ");
            var patterns = parts[0].split(" ");
            var outputValues = parts[1].split(" ");
            var map = parsePatterns(patterns);
            var numberString = new StringBuilder();

            for (var value: outputValues) {
                var chars = new HashSet<>(Chars.asList(value.toCharArray()));

                for (var entry: map.entrySet()) {
                    if (entry.getValue().equals(chars)) {
                        numberString.append(entry.getKey());
                        break;
                    }
                }
            }

            sum += Integer.parseInt(numberString.toString());
        }

        return String.valueOf(sum);
    }

    private Map<Integer, Set<Character>> parsePatterns(String[] patterns) {
        var map = new HashMap<Integer, Set<Character>>();
        var byCount = new HashMap<Integer, List<Set<Character>>>();

        for (var pattern: patterns) {
            var patternChars = new HashSet<>(Chars.asList(pattern.toCharArray()));
            var patternLength = pattern.length();
            var list = byCount.getOrDefault(patternLength, new ArrayList<>());
            list.add(patternChars);
            byCount.put(patternLength, list);
        }

        map.put(1, byCount.get(2).getFirst());
        map.put(4, byCount.get(4).getFirst());
        map.put(7, byCount.get(3).getFirst());
        map.put(8, byCount.get(7).getFirst());

        // 9 is the only 6 character pattern that contains the number 4 pattern
        for (var charSet: byCount.get(6)) {
            if (charSet.containsAll(map.get(4))) {
                byCount.get(6).remove(charSet);
                map.put(9, charSet);
                break;
            }
        }

        // now 0 will be the only 6 character patterns containing the numbers 7 pattern
        // then the only 6 character pattern left will be 6
        for (var charSet: byCount.get(6)) {
            if (charSet.containsAll(map.get(7))) {
                byCount.get(6).remove(charSet);
                map.put(0, charSet);
                map.put(6, byCount.get(6).getFirst());
                break;
            }
        }

        // 3 is the only 5 character pattern that contains the number 7 pattern
        for (var charSet: byCount.get(5)) {
            if (charSet.containsAll(map.get(7))) {
                byCount.get(5).remove(charSet);
                map.put(3, charSet);
                break;
            }
        }

        // now 5 will be the only 5 character patterns that is contained fully in 6
        // then 2 will be the only 5 character pattern left
        for (var charSet: byCount.get(5)) {
            if (map.get(6).containsAll(charSet)) {
                byCount.get(5).remove(charSet);
                map.put(5, charSet);
                map.put(2, byCount.get(5).getFirst());
                break;
            }
        }

        return map;
    }
}

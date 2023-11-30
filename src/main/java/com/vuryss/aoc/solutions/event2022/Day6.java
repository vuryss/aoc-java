package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.HashSet;
import java.util.Map;

public class Day6 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
            "7",
            "bvwbjplbgvbhsrlpgdmjqwftvncz",
            "5",
            "nppdvjthqldpwncqszvftbrmjlhg",
            "6",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
            "10",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw",
            "11"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
            "19",
            "bvwbjplbgvbhsrlpgdmjqwftvncz",
            "23",
            "nppdvjthqldpwncqszvftbrmjlhg",
            "23",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
            "29",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw",
            "26"
        );
    }

    @Override
    public String part1Solution(String input) {
        return getFirstBatchOfUniqueCharactersLastPosition(input, 4);
    }

    @Override
    public String part2Solution(String input) {
        return getFirstBatchOfUniqueCharactersLastPosition(input, 14);
    }

    private String getFirstBatchOfUniqueCharactersLastPosition(String input, int uniqueCharactersCount) {
        var characters = input.toCharArray();
        var index = 0;

        while (true) {
            var set = new HashSet<Character>();

            for (var i = 0; i < uniqueCharactersCount; i++) {
                set.add(characters[index + i]);
            }

            if (set.size() == uniqueCharactersCount) {
                return String.valueOf(index + uniqueCharactersCount);
            }

            index++;
        }
    }
}

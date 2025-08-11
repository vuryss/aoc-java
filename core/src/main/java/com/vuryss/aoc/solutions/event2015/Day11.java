package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    private String part1Password;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "abcdefgh", "abcdffaa",
            "ghijklmn", "ghjaabcc"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var password = input.trim();

        part1Password = findNextValidPassword(password);

        return part1Password;
    }

    @Override
    public String part2Solution(String input) {
        return findNextValidPassword(part1Password);
    }

    private String findNextValidPassword(String password) {
        var charArray = password.toCharArray();
        var index = charArray.length - 1;

        do {
            for (var i = 0; i < charArray.length; i++) {
                if (++charArray[index - i] <= 'z') {
                    break;
                }

                charArray[index - i] = 'a';
            }

            index = charArray.length - 1;
        } while (!isValidPassword(charArray));

        return new String(charArray);
    }

    private boolean isValidPassword(char[] password) {
        return hasIncreasingStraight(password) &&
               !containsForbiddenCharacters(password) &&
               hasTwoPairs(password);
    }

    private boolean hasIncreasingStraight(char[] password) {
        for (var i = 0; i < password.length - 2; i++) {
            if (password[i] + 1 == password[i + 1] && password[i] + 2 == password[i + 2]) {
                return true;
            }
        }

        return false;
    }

    private boolean containsForbiddenCharacters(char[] password) {
        for (var c: password) {
            if (c == 'i' || c == 'o' || c == 'l') {
                return true;
            }
        }

        return false;
    }

    private boolean hasTwoPairs(char[] password) {
        var pairCount = 0;
        var lastPair = ' ';

        for (var i = 0; i < password.length - 1; i++) {
            if (password[i] == password[i + 1] && password[i] != lastPair) {
                pairCount++;
                lastPair = password[i];
                i++;
            }
        }

        return pairCount >= 2;
    }

}

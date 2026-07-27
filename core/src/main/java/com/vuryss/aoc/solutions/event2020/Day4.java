package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
            byr:1937 iyr:2017 cid:147 hgt:183cm
            
            iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
            hcl:#cfa07d byr:1929
            
            hcl:#ae17e1 iyr:2013
            eyr:2024
            ecl:brn pid:760753108 byr:1931
            hgt:179cm
            
            hcl:#cfa07d eyr:2025 pid:166559648
            iyr:2011 ecl:brn hgt:59in
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            eyr:1972 cid:100
            hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
            
            iyr:2019
            hcl:#602927 eyr:1967 hgt:170cm
            ecl:grn pid:012533040 byr:1946
            
            hcl:dab227 iyr:2012
            ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
            
            hgt:59cm ecl:zzz
            eyr:2038 hcl:74454a iyr:2023
            pid:3556412378 byr:2007
            """,
            "0",
            """
            pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
            hcl:#623a2f
            
            eyr:2029 ecl:blu cid:129 byr:1989
            iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
            
            hcl:#888785
            hgt:164cm byr:2001 iyr:2015 cid:88
            pid:545766238 ecl:hzl
            eyr:2022
            
            iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
            """,
            "4"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var batches = input.trim().split("\n\n");
        var counter = 0;
        var requiredSet = Set.of("ecl", "pid", "eyr", "hcl", "byr", "iyr", "hgt");

        for (var batch : batches) {
            var fields = Regex.matchAll("\\b[a-z]{3}:", batch);
            var present = fields.stream().map(field -> field.substring(0, field.length() - 1)).collect(Collectors.toSet());

            if (present.containsAll(requiredSet)) counter++;
        }

        return counter + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var batches = input.trim().split("\n\n");
        var counter = 0;
        var requiredSet = Set.of("ecl", "pid", "eyr", "hcl", "byr", "iyr", "hgt");

        for (var batch : batches) {
            var fields = Regex.matchAll("\\b[a-z]{3}:", batch);
            var present = fields.stream().map(field -> field.substring(0, field.length() - 1)).collect(Collectors.toSet());

            if (
                present.containsAll(requiredSet)
                && Regex.matches("\\bbyr:(19[2-9][0-9]|200[0-2])\\b", batch)
                && Regex.matches("\\biyr:(201[0-9]|2020)\\b", batch)
                && Regex.matches("\\beyr:(202[0-9]|2030)\\b", batch)
                && Regex.matches("\\bhgt:((1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in)\\b", batch)
                && Regex.matches("\\bhcl:#[a-f0-9]{6}\\b", batch)
                && Regex.matches("\\becl:(amb|blu|brn|gry|grn|hzl|oth)\\b", batch)
                && Regex.matches("\\bpid:\\d{9}\\b", batch)
            ) counter++;
        }

        return counter + "";
    }
}

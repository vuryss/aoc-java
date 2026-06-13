package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import lombok.Data;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    private final Pattern pattern = Pattern.compile("(?<units>\\d+).*?(?<hp>\\d+).*?(?<damage>\\d+)\\s(?<type>\\w+)\\sdamage.*?(?<initiative>\\d+)");
    private final Pattern weakPattern = Pattern.compile("weak\\sto\\s([\\w,\\s]+)");
    private final Pattern immunePattern = Pattern.compile("immune\\sto\\s([\\w,\\s]+)");

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Immune System:
            17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
            989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
            
            Infection:
            801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
            4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
            """,
            "5216"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Immune System:
            17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
            989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
            
            Infection:
            801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
            4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
            """,
            "51"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return runBattle(parseGroups(input)).units + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var groups = parseGroups(input);

        for (var i = 0; true; i++) {
            var boost = i;
            var newGroups = groups.stream()
                .map(Group::new)
                .peek(g -> g.attackDamage += g.type == GroupType.IMMUNE_SYSTEM ? boost : 0)
                .collect(Collectors.toList());
            var result = runBattle(newGroups);

            if (result.winner == GroupType.IMMUNE_SYSTEM) {
                return result.units + "";
            }
        }
    }

    private List<Group> parseGroups(String input) {
        var parts = input.trim().split("\n\n");
        var immuneInputLines = parts[0].lines().skip(1).toList();
        var infectionInputLines = parts[1].lines().skip(1).toList();
        var groups = new ArrayList<Group>();

        for (var infectionLine : infectionInputLines) {
            var group = parseGroup(infectionLine, GroupType.INFECTION);

            if (group != null) {
                groups.add(group);
            }
        }

        for (var immuneLine : immuneInputLines) {
            var group = parseGroup(immuneLine, GroupType.IMMUNE_SYSTEM);

            if (group != null) {
                groups.add(group);
            }
        }

        return groups;
    }

    private Group parseGroup(String line, GroupType groupType) {
        var matcher = pattern.matcher(line);
        if (!matcher.find()) return null;

        var group = new Group();
        group.type = groupType;
        group.unitCount = Long.parseLong(matcher.group("units"));
        group.hitPoints = Long.parseLong(matcher.group("hp"));
        group.attackDamage = Long.parseLong(matcher.group("damage"));
        group.attackType = AttackType.valueOf(matcher.group("type").toUpperCase());
        group.initiative = Long.parseLong(matcher.group("initiative"));

        var weakMatcher = weakPattern.matcher(line);
        if (weakMatcher.find()) {
            var weaknesses = weakMatcher.group(1).split(",\\s*");
            for (var weakness : weaknesses) {
                group.weaknesses.add(AttackType.valueOf(weakness.toUpperCase()));
            }
        }

        var immuneMatcher = immunePattern.matcher(line);
        if (immuneMatcher.find()) {
            var immunities = immuneMatcher.group(1).split(",\\s*");
            for (var immunity : immunities) {
                group.immunities.add(AttackType.valueOf(immunity.toUpperCase()));
            }
        }

        return group;
    }

    private Result runBattle(List<Group> groups) {
        while (groups.stream().map(Group::getType).distinct().count() > 1) {
            // Target selection phase
            groups.sort(Comparator.comparingLong(Group::effectivePower).thenComparingLong(Group::getInitiative).reversed());

            for (var group : groups) {
                group.target = groups.stream()
                    .filter(t -> t.type != group.type && !t.isTarget && group.calculateDamageTo(t) > 0)
                    .max(Comparator.comparingLong(group::calculateDamageTo)
                        .thenComparingLong(Group::effectivePower)
                        .thenComparingLong(Group::getInitiative))
                    .orElse(null);

                if (group.target != null) {
                    group.target.isTarget = true;
                }
            }

            // Attack phase
            long totalUnitsKilled = 0;
            groups.sort(Comparator.comparingLong(Group::getInitiative).reversed());

            for (var group : groups) {
                if (group.unitCount < 1 || group.target == null) continue;
                var damage = group.calculateDamageTo(group.target);
                var unitsKilled = damage / group.target.hitPoints;
                group.target.unitCount -= unitsKilled;
                totalUnitsKilled += unitsKilled;
            }

            if (totalUnitsKilled == 0) return new Result(GroupType.NONE, 0);

            // Reset
            groups.removeIf(g -> g.unitCount < 1);
            groups.forEach(g -> { g.isTarget = false; g.target = null; });
        }

        return new Result(groups.getFirst().type, groups.stream().mapToLong(Group::getUnitCount).sum());
    }

    @Data
    private static class Group {
        public Group() {}
        public Group(Group group) {
            this.type = group.type;
            this.unitCount = group.unitCount;
            this.hitPoints = group.hitPoints;
            this.attackDamage = group.attackDamage;
            this.attackType = group.attackType;
            this.initiative = group.initiative;
            this.weaknesses = new HashSet<>(group.weaknesses);
            this.immunities = new HashSet<>(group.immunities);
        }

        public GroupType type;
        public long unitCount;
        public long hitPoints;
        public long attackDamage;
        public AttackType attackType;
        public long initiative;
        public Set<AttackType> weaknesses = new HashSet<>();
        public Set<AttackType> immunities = new HashSet<>();
        public boolean isTarget = false;
        public Group target = null;

        public long effectivePower() { return unitCount * attackDamage; }

        public long calculateDamageTo(Group target) {
            if (target.immunities.contains(attackType)) return 0;
            return target.weaknesses.contains(attackType) ? effectivePower() * 2 : effectivePower();
        }
    }

    private record Result(GroupType winner, long units) {}
    private enum GroupType { IMMUNE_SYSTEM, INFECTION, NONE }
    private enum AttackType { FIRE, COLD, RADIATION, BLUDGEONING, SLASHING }
}

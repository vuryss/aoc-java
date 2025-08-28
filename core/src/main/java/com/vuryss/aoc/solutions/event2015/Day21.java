package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
    final Map<Integer, Item> weapons = Map.of(
        8, new Item(8, 4, 0),
        10, new Item(10, 5, 0),
        25, new Item(25, 6, 0),
        40, new Item(40, 7, 0),
        74, new Item(74, 8, 0)
    );

    final Map<Integer, Item> armors = Map.of(
        0, new Item(0, 0, 0), // no armor
        13, new Item(13, 0, 1),
        31, new Item(31, 0, 2),
        53, new Item(53, 0, 3),
        75, new Item(75, 0, 4),
        102, new Item(102, 0, 5)
    );

    final Map<Integer, Item> rings = Map.of(
        25, new Item(25, 1, 0),
        50, new Item(50, 2, 0),
        100, new Item(100, 3, 0),
        20, new Item(20, 0, 1),
        40, new Item(40, 0, 2),
        80, new Item(80, 0, 3)
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input) {
        var numbers = StringUtil.ints(input);
        var bossItem = new Item(0, numbers.get(1), numbers.get(2));

        for (var item: itemsByCost()) {
            if (playerWins(item, bossItem, numbers.getFirst())) {
                return String.valueOf(item.cost);
            }
        }

        return "-dunno-";
    }

    @Override
    public String part2Solution(String input) {
        var numbers = StringUtil.ints(input);
        var bossItem = new Item(0, numbers.get(1), numbers.get(2));

        for (var item: itemsByCost().reversed()) {
            if (!playerWins(item, bossItem, numbers.getFirst())) {
                return String.valueOf(item.cost);
            }
        }

        return "-dunno-";
    }

    private boolean playerWins(Item player, Item boss, int bossHp) {
        var playerHp = 100;
        var playerTurn = true;

        while (playerHp > 0 && bossHp > 0) {
            if (playerTurn) {
                bossHp -= Math.max(1, player.damage - boss.armor);
            } else {
                playerHp -= Math.max(1, boss.damage - player.armor);
            }

            playerTurn = !playerTurn;
        }

        return playerHp > 0;
    }

    private List<Item> itemsByCost() {
        var rings = new ArrayList<>(this.rings.values()); // 1 ring
        rings.add(new Item(0, 0, 0)); // 0 rings

        for (var ring1: this.rings.values()) {
            for (var ring2: this.rings.values()) {
                if (ring1 == ring2) {
                    continue;
                }

                rings.add(new Item(ring1.cost + ring2.cost, ring1.damage + ring2.damage, ring1.armor + ring2.armor));
            }
        }

        var items = new ArrayList<Item>();

        for (var weapon: weapons.values()) {
            for (var armor: armors.values()) {
                for (var ring: rings) {
                    items.add(new Item(
                        weapon.cost + armor.cost + ring.cost,
                        weapon.damage + armor.damage + ring.damage,
                        weapon.armor + armor.armor + ring.armor
                    ));
                }
            }
        }

        items.sort(Comparator.comparingInt(i -> i.cost));

        return items;
    }

    record Item(int cost, int damage, int armor) {}
}

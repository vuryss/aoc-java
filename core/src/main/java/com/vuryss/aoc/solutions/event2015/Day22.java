package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    enum Spell {
        MAGIC_MISSILE(53, 0),
        DRAIN(73, 0),
        SHIELD(113, 6),
        POISON(173, 6),
        RECHARGE(229, 5);

        final int cost;
        final int duration;

        Spell(int cost, int duration) {
            this.cost = cost;
            this.duration = duration;
        }
    }

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
        return playGame(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return playGame(input, true);
    }

    private String playGame(String input, boolean hardMode) {
        var inputNumbers = StringUtil.ints(input);
        var playerState = new Stats(50, 500, 0);
        var bossState = new Stats(inputNumbers.get(0), 0, inputNumbers.get(1));
        var isPlayerTurn = true;

        var heap = new PriorityQueue<Game>(Comparator.comparingInt(a -> a.manaSpent));
        heap.add(new Game(playerState, bossState, Map.of(), 0, isPlayerTurn, hardMode));

        while (!heap.isEmpty()) {
            var state = heap.poll();

            if (state.player.health <= 0) {
                continue;
            }

            if (state.boss.health <= 0) {
                return String.valueOf(state.manaSpent);
            }

            if (state.isPlayerTurn) {
                for (var spell: Spell.values()) {
                    var newGame = state.castSpell(spell);

                    if (newGame != null) {
                        heap.add(newGame);
                    }
                }
            } else {
                var newGame = state.bossTurn();
                heap.add(newGame);
            }
        }

        return "-not found-";
    }

    record Game(
        Stats player,
        Stats boss,
        Map<Spell, SpellState> activeSpells,
        int manaSpent,
        boolean isPlayerTurn,
        boolean hardMode
    ) {
        public Game castSpell(Spell spell) {
            // Check if we can afford the spell, otherwise no need to continue
            if (player.mana < spell.cost) {
                return null;
            }

            var newActiveSpells = new HashMap<Spell, SpellState>();
            var newPlayer = player.spendMana(spell.cost);
            var newBoss = boss;

            if (hardMode) {
                newPlayer = newPlayer.takeDamage(1);
            }

            // Apply all active spell effects
            for (var spellState: activeSpells.values()) {
                switch (spellState.spell) {
                    case POISON -> newBoss = newBoss.takeDamage(3);
                    case RECHARGE -> newPlayer = newPlayer.addMana(101);
                }

                if (spellState.turnsLeft > 1) {
                    newActiveSpells.put(spellState.spell, new SpellState(spellState.spell, spellState.turnsLeft - 1));
                }
            }

            // Check if the spell is already active, otherwise no need to continue
            if (newActiveSpells.containsKey(spell)) {
                return null;
            }

            switch (spell) {
                case MAGIC_MISSILE -> newBoss = newBoss.takeDamage(4);
                case DRAIN -> {
                    newBoss = newBoss.takeDamage(2);
                    newPlayer = newPlayer.addHealth(2);
                }
                case SHIELD, POISON, RECHARGE -> newActiveSpells.put(spell, new SpellState(spell, spell.duration));
            }

            return new Game(newPlayer, newBoss, newActiveSpells, manaSpent + spell.cost, !isPlayerTurn, hardMode);
        }

        public Game bossTurn() {
            var newActiveSpells = new HashMap<Spell, SpellState>();
            var newPlayer = new Stats(player.health, player.mana, 0);
            var newBoss = new Stats(boss.health, boss.mana, boss.attack);
            var playerArmor = 0;

            // Apply all active spell effects
            for (var spellState: activeSpells.values()) {
                switch (spellState.spell) {
                    case SHIELD -> playerArmor = 7;
                    case POISON -> newBoss = newBoss.takeDamage(3);
                    case RECHARGE -> newPlayer = newPlayer.addMana(101);
                }

                if (spellState.turnsLeft > 1) {
                    newActiveSpells.put(spellState.spell, new SpellState(spellState.spell, spellState.turnsLeft - 1));
                }
            }

            newPlayer = newPlayer.takeDamage(Math.max(1, boss.attack - playerArmor));

            return new Game(newPlayer, newBoss, newActiveSpells, manaSpent, !isPlayerTurn, hardMode);
        }
    }

    record Stats (int health, int mana, int attack) {
        public Stats takeDamage(int damage) {
            return new Stats(health - damage, mana, attack);
        }
        public Stats addHealth(int health) {
            return new Stats(this.health + health, mana, attack);
        }
        public Stats addMana(int mana) {
            return new Stats(health, this.mana + mana, attack);
        }
        public Stats spendMana(int mana) {
            return new Stats(health, this.mana - mana, attack);
        }
    }

    record SpellState (Spell spell, int turnsLeft) {}
}

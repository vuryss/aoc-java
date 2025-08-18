package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
            Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
            """,
            "62842880"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
            Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
            """,
            "57600000"
        );
    }

    @Override
    public String part1Solution(String input) {
        var ingredients = parseIngredients(input);

        return String.valueOf(calculateMaxScore(ingredients, 100, new Ingredients(0, 0, 0, 0, 0), 0));
    }

    @Override
    public String part2Solution(String input) {
        var ingredients = parseIngredients(input);

        return String.valueOf(calculateMaxScore(ingredients, 100, new Ingredients(0, 0, 0, 0, 0), 500));
    }

    private LinkedList<Ingredients> parseIngredients(String input) {
        var lines = input.trim().split("\n");
        var ingredients = new LinkedList<Ingredients>();

        for (var line: lines) {
            var parts = StringUtil.sints(line);
            ingredients.add(new Ingredients(parts.get(0), parts.get(1), parts.get(2), parts.get(3), parts.get(4)));
        }

        return ingredients;
    }

    private int calculateMaxScore(LinkedList<Ingredients> ingredients, int teaspoonsLeft, Ingredients state, int targetCalories) {
        if (ingredients.size() == 1) {
            return state.add(ingredients.getFirst().getTeaspoons(teaspoonsLeft)).score(targetCalories);
        }

        var maxTeaspoons = teaspoonsLeft - ingredients.size() + 1;
        var maxScore = 0;

        for (var teaspoons = 0; teaspoons <= maxTeaspoons; teaspoons++) {
            var remainingIngredients = new LinkedList<>(ingredients);
            var newState = state.add(Objects.requireNonNull(remainingIngredients.poll()).getTeaspoons(teaspoons));

            maxScore = Math.max(maxScore, calculateMaxScore(remainingIngredients, teaspoonsLeft - teaspoons, newState, targetCalories));
        }

        return maxScore;
    }

    record Ingredients(int capacity, int durability, int flavor, int texture, int calories) {
        public int score(int targetCalories) {
            if (0 != targetCalories && targetCalories != calories) {
                return 0;
            }

            return Math.max(0, capacity) * Math.max(0, durability) * Math.max(0, flavor) * Math.max(0, texture);
        }

        public Ingredients add(Ingredients other) {
            return new Ingredients(
                capacity + other.capacity,
                durability + other.durability,
                flavor + other.flavor,
                texture + other.texture,
                calories + other.calories
            );
        }

        public Ingredients getTeaspoons(int teaspoons) {
            return new Ingredients(
                capacity * teaspoons,
                durability * teaspoons,
                flavor * teaspoons,
                texture * teaspoons,
                calories * teaspoons
            );
        }
    }
}

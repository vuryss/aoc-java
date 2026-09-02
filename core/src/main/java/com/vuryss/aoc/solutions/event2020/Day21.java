package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
            """,
            "mxmxvkd,sqjhc,fvjkl"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var foods = parseInput(input);
        var allergenIngredients = mapAllergensToIngredientsAndClearFood(foods);

        return foods.stream().mapToLong(food -> food.ingredients.size()).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var foods = parseInput(input);
        var allergenIngredients = mapAllergensToIngredientsAndClearFood(foods);

        return allergenIngredients.keySet().stream().sorted()
            .map(allergenIngredients::get)
            .collect(Collectors.joining(","));
    }

    private Map<String, String> mapAllergensToIngredientsAndClearFood(List<Food> foods) {
        var allergenIngredients = new HashMap<String, String>();
        var unmappedAllergens = foods.stream().map(food -> food.allergens).flatMap(Set::stream).collect(Collectors.toSet());

        while (!unmappedAllergens.isEmpty()) {
            for (var allergen : new HashSet<>(unmappedAllergens)) {
                var ingredientsIntersection = foods.stream()
                    .filter(food -> food.allergens.contains(allergen))
                    .map(food -> food.ingredients)
                    .reduce((a, b) -> {
                        var intersection = new HashSet<>(a);
                        intersection.retainAll(b);
                        return intersection;
                    })
                    .get();

                if (ingredientsIntersection.size() == 1) {
                    var ingredient = ingredientsIntersection.iterator().next();
                    allergenIngredients.put(allergen, ingredient);
                    unmappedAllergens.remove(allergen);
                    foods.forEach(f -> {
                        f.ingredients.remove(ingredient);
                        f.allergens.remove(allergen);
                    });
                }
            }
        }

        return allergenIngredients;
    }

    private List<Food> parseInput(String input) {
        return Arrays.stream(input.trim().split("\n"))
            .map(line -> line.substring(0, line.length() - 1).split(" \\(contains "))
            .map(p -> new Food(
                new HashSet<>(Arrays.asList(p[0].split(" "))),
                new HashSet<>(Arrays.asList(p[1].split(", ")))
            ))
            .toList();
    }

    private record Food(HashSet<String> ingredients, HashSet<String> allergens) {}
}

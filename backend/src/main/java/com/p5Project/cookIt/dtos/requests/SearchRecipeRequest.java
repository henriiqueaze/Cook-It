package com.p5Project.cookIt.dtos.requests;

import java.util.List;
import java.util.Objects;

public record SearchRecipeRequest(
        List<String> ingredients,
        String recipeName,
        boolean exactMatch,
        String sortBy
) {

    public List<String> normalizedIngredients() {
        if (ingredients == null) {
            return List.of();
        }

        return ingredients.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(ingredient -> !ingredient.isBlank())
                .map(String::toLowerCase)
                .toList();
    }

    public String normalizedRecipeName() {
        return recipeName == null ? "" : recipeName.trim().toLowerCase();
    }
}

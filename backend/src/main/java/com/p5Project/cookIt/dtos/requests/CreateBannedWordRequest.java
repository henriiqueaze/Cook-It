package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBannedWordRequest(
        @NotBlank(message = "Banned word is required") String term,
        @NotNull(message = "Recipe flag is required") Boolean appliesToRecipes,
        @NotNull(message = "Ingredient flag is required") Boolean appliesToIngredients,
        @NotNull(message = "Comment flag is required") Boolean appliesToComments
) {
}
package com.p5Project.cookIt.dtos.requests;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateRecipeRequest(
        @NotBlank(message = "Recipe name is required") String name,
        String description,
        @NotNull(message = "Preparation time is required") @Positive(message = "Preparation time must be positive") Integer prepTime,
        @NotNull(message = "Portions are required") @Positive(message = "Portions must be positive") Integer portions,
        @NotNull(message = "Ingredients are required") List<RecipeIngredientDTO> ingredients,
        @NotNull(message = "Instructions are required") List<String> instructions
) {
}
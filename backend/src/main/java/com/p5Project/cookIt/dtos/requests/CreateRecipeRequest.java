package com.p5Project.cookIt.dtos.requests;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateRecipeRequest {

    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotNull(message = "Preparation time is required")
    @Positive(message = "Preparation time must be positive")
    private Integer prepTime;

    @NotNull(message = "Portions are required")
    @Positive(message = "Portions must be positive")
    private Integer portions;

    @NotNull(message = "Ingredients are required")
    private List<RecipeIngredientDTO> ingredients;

    @NotNull(message = "Instructions are required")
    private List<String> instructions;
}
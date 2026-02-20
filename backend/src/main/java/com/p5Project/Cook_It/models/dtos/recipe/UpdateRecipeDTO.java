package com.p5Project.Cook_It.models.dtos.recipe;

import com.p5Project.Cook_It.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRecipeDTO {
    private String title;
    private String description;
    private String steps;
    private Integer servings;
    private Integer prepTimeMinutes;
    private Boolean published;
    private List<RecipeIngredientCreateDTO> ingredients;
    private List<UUID> tagIds;
}
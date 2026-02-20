package com.p5Project.cookIt.models.dtos.recipe;

import com.p5Project.cookIt.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeDTO {
    @NotNull
    private UUID authorId;

    @NotBlank
    private String title;

    private String description;
    private String steps;

    private Integer servings;
    private Integer prepTimeMinutes;
    private Boolean published = false;

    private List<RecipeIngredientCreateDTO> ingredients;
    private List<UUID> tagIds;
}

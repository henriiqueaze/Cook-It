package com.p5Project.cookIt.models.dtos.recipeIngredient;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RecipeIngredientDTO {

    private UUID id;
    private UUID recipeId;
    private UUID ingredientId;
    private Double quantity;
    private String unit;
    private String notes;
}
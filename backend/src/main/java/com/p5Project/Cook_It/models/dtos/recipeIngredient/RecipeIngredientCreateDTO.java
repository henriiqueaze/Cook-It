package com.p5Project.Cook_It.models.dtos.recipeIngredient;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RecipeIngredientCreateDTO {
    @NotNull
    private UUID ingredientId;

    @NotNull
    private Double quantity;

    private String unit;
    private String notes;
}

package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Ingredient;
import com.p5Project.cookIt.models.entities.Recipe;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {

    private UUID id;
    private Recipe recipe;
    private Ingredient ingredient;
    private Double quantity;
    private String unit;
    private String notes;
}
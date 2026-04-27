package com.p5Project.cookIt.dtos.requests;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;

import java.util.List;

public record UpdateRecipeRequest(
        String name,
        String description,
        Integer prepTime,
        Integer portions,
        List<RecipeIngredientDTO> ingredients,
        List<String> instructions
) {
}
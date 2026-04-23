package com.p5Project.cookIt.dtos.requests;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRecipeRequest {

    private String name;

    private String description;

    private Integer prepTime;

    private Integer portions;

    private List<RecipeIngredientDTO> ingredients;

    private List<String> instructions;
}
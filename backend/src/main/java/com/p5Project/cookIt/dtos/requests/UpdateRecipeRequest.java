package com.p5Project.cookIt.dtos.requests;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRecipeRequest {

    private String name;

    private Integer prepTime;

    private List<RecipeIngredientDTO> ingredients;

    private List<String> instructions;

    private String image;

}
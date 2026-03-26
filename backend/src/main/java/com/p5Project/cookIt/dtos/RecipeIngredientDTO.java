package com.p5Project.cookIt.dtos;

import lombok.Data;

@Data
public class RecipeIngredientDTO {

    private String ingredient;

    private Double quantity;

    private String unit;
}
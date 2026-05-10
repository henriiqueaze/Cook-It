package com.p5Project.cookIt.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RecipeIngredient {

    private String ingredient;

    private Double quantity;

    private String unit;
}
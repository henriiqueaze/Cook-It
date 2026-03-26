package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateIngredientRequest {

    @NotBlank(message = "Ingredient name is required")
    private String name;
}
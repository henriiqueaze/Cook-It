package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateIngredientRequest(
        @NotBlank(message = "Ingredient name is required") String name
) {
}
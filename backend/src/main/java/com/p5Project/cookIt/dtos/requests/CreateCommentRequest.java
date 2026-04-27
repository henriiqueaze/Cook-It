package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotNull(message = "Recipe id is required") String recipeId,
        @NotBlank(message = "Comment text cannot be empty") String text
) {
}
package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotNull(message = "Recipe id is required")
    private String recipeId;

    @NotBlank(message = "Comment text cannot be empty")
    private String text;
}
package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank(message = "Comment text cannot be empty") String text
) {
}
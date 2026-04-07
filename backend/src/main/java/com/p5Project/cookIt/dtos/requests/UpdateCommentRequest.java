package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCommentRequest {

    @NotBlank(message = "Comment text cannot be empty")
    private String text;
}
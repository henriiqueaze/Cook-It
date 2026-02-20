package com.p5Project.Cook_It.models.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDTO {

    @NotNull
    private UUID recipeId;

    @NotNull
    private UUID userId;

    @NotBlank
    private String content;
}
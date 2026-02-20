package com.p5Project.Cook_It.models.dtos.recipeTag;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeTagDTO {
    @NotNull
    private UUID recipeId;

    @NotNull
    private UUID tagId;
}

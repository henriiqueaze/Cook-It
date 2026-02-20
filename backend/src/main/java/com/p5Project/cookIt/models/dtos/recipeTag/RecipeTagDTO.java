package com.p5Project.cookIt.models.dtos.recipeTag;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeTagDTO {
    private UUID id;
    private UUID recipeId;
    private UUID tagId;
}
package com.p5Project.Cook_It.models.dtos.ingredient;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {

    private UUID id;
    private String name;
    private String normalizedName;
    private String unitDefault;
    private Instant createdAt;
}

package com.p5Project.cookIt.models.dtos.pantryItem;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePantryItemDTO {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID ingredientId;

    @NotNull
    private Double quantity;

    private String unit;
    private Instant expiresAt;
}
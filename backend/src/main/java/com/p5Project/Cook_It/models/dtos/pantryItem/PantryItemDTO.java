package com.p5Project.Cook_It.models.dtos.pantryItem;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PantryItemDTO {
    private UUID id;
    private UUID userId;
    private UUID ingredientId;
    private Double quantity;
    private String unit;
    private Instant addedAt;
    private Instant expiresAt;
}
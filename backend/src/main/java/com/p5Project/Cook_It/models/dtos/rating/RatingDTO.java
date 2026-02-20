package com.p5Project.Cook_It.models.dtos.rating;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {
    private UUID id;
    private UUID recipeId;
    private UUID userId;
    private Short score;
    private String review;
    private Instant createdAt;
}
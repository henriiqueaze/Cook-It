package com.p5Project.cookIt.models.dtos.rating;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRatingDTO {

    @NotNull
    private UUID recipeId;

    @NotNull
    private UUID userId;

    @NotNull
    @Min(1)
    @Max(5)
    private Short score;

    private String review;
}
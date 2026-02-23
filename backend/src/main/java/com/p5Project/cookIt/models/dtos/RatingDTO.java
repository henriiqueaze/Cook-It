package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.User;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {

    private UUID id;
    private Recipe recipe;
    private User user;
    private Short score;
    private String review;
    private Instant createdAt;
}
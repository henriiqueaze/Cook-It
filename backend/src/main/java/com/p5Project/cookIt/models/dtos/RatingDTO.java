package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO extends RepresentationModel<RatingDTO> {

    private UUID id;
    private Recipe recipe;
    private User user;
    private Short score;
    private String review;
    private Instant createdAt;
}
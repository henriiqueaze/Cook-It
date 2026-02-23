package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Ingredient;
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
public class PantryItemDTO extends RepresentationModel<PantryItemDTO> {

    private UUID id;
    private User user;
    private Ingredient ingredient;
    private Double quantity;
    private String unit;
    private Instant addedAt;
    private Instant expiresAt;
}
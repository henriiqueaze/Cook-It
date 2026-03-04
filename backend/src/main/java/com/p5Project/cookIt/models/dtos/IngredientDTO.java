package com.p5Project.cookIt.models.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO extends RepresentationModel<IngredientDTO> {

    private UUID id;
    private String name;
}

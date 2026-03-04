package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Ingredient;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO extends RepresentationModel<RecipeIngredientDTO> {

    private UUID id;
    private Ingredient ingredient;
    private Double quantity;
    private String unit;
}
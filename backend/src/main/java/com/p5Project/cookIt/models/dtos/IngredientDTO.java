package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.PantryItem;
import com.p5Project.cookIt.models.entities.RecipeIngredient;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO extends RepresentationModel<ImageDTO> {

    private UUID id;
    private String name;
    private String normalizedName;
    private String unitDefault;
    private Instant createdAt;
    private List<RecipeIngredient> recipeIngredients;
    private List<PantryItem> pantryItems;
}

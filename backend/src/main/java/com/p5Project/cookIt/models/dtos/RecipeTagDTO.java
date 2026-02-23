package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.Tag;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeTagDTO {

    private UUID id;
    private Recipe recipe;
    private Tag tag;
}
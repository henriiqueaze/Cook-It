package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.RecipeTag;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {

    private UUID id;
    private String name;
    private List<RecipeTag> recipeTags;
}
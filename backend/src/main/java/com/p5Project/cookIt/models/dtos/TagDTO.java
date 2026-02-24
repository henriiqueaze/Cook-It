package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.RecipeTag;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO extends RepresentationModel<TagDTO> {

    private UUID id;
    private String name;
    private List<RecipeTag> recipeTags;
}
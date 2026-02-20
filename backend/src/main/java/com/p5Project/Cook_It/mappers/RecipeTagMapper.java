package com.p5Project.Cook_It.mappers;

import com.p5Project.Cook_It.models.dtos.recipeTag.RecipeTagDTO;
import com.p5Project.Cook_It.models.entities.RecipeTag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RecipeTagMapper {

    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "tagId", source = "tag.id")
    RecipeTagDTO toDto(RecipeTag entity);
}
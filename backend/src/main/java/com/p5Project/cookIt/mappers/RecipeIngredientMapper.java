package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.entities.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {

    @Mapping(source = "ingredient", target = "ingredient")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unit", target = "unit")
    RecipeIngredient toEntity(RecipeIngredientDTO dto);

    @Mapping(source = "ingredient", target = "ingredient")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unit", target = "unit")
    RecipeIngredientDTO toDTO(RecipeIngredient entity);
}
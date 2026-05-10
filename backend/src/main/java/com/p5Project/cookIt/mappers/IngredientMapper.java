package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.IngredientDTO;
import com.p5Project.cookIt.entities.Ingredient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDTO toDTO(Ingredient ingredient);

    Ingredient toEntity(IngredientDTO dto);

    List<IngredientDTO> toDTOList(List<Ingredient> ingredients);

}
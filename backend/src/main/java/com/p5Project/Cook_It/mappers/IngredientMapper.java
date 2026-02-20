package com.p5Project.Cook_It.mappers;

import com.p5Project.Cook_It.models.dtos.ingredient.CreateIngredientDTO;
import com.p5Project.Cook_It.models.dtos.ingredient.IngredientDTO;
import com.p5Project.Cook_It.models.entities.Ingredient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDTO toDto(Ingredient ingredient);

    Ingredient toEntity(CreateIngredientDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateIngredientDTO dto, @MappingTarget Ingredient entity);

    default Ingredient fromId(java.util.UUID id) {
        if (id == null) return null;
        Ingredient i = new Ingredient();
        i.setId(id);
        return i;
    }
}

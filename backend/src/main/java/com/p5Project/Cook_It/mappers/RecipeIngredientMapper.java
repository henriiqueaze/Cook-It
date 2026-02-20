package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import com.p5Project.Cook_It.models.dtos.recipeIngredient.RecipeIngredientDTO;
import com.p5Project.Cook_It.models.entities.Ingredient;
import com.p5Project.Cook_It.models.entities.RecipeIngredient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {

    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "ingredientId", source = "ingredient.id")
    RecipeIngredientDTO toDto(RecipeIngredient entity);

    @Mapping(target = "ingredient", expression = "java(ingredientFromId(dto.getIngredientId()))")
    RecipeIngredient toEntity(RecipeIngredientCreateDTO dto); // se lembre de colocar recipeId no service :)

    default Ingredient ingredientFromId(java.util.UUID id) {
        if (id == null) return null;
        Ingredient i = new Ingredient();
        i.setId(id);
        return i;
    }
}

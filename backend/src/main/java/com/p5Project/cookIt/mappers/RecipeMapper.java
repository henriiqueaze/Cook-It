package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = RecipeIngredientMapper.class)
public interface RecipeMapper {

    @Mappings({
            @Mapping(source = "author.id", target = "authorId"),
            @Mapping(source = "author.name", target = "authorName"),
            @Mapping(source = "author.photo", target = "authorPhoto"),
            @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "ingredients", target = "ingredients"),
            @Mapping(source = "instructions", target = "instructions")
    })
    RecipeDTO toDTO(Recipe recipe);

    List<RecipeDTO> toDTOList(List<Recipe> recipes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecipeFromRequest(UpdateRecipeRequest request, @MappingTarget Recipe recipe);
}
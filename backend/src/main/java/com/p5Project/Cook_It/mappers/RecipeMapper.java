package com.p5Project.Cook_It.mappers;

import com.p5Project.Cook_It.models.dtos.recipe.CreateRecipeDTO;
import com.p5Project.Cook_It.models.dtos.recipe.RecipeDTO;
import com.p5Project.Cook_It.models.dtos.recipe.UpdateRecipeDTO;
import com.p5Project.Cook_It.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import com.p5Project.Cook_It.models.dtos.tag.TagDTO;
import com.p5Project.Cook_It.models.entities.*;
import org.mapstruct.*;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        ImageMapper.class,
        RecipeIngredientMapper.class,
        RecipeTagMapper.class,
        TagMapper.class
})
public interface RecipeMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "tags", source = "recipeTags")
    RecipeDTO toDto(Recipe recipe);
    @Mapping(target = "author", expression = "java(userFromId(dto.getAuthorId()))")
    @Mapping(target = "published", source = "published") // recipeIngredients e recipeTags serão gerenciados no service :)
    Recipe toEntity(CreateRecipeDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateRecipeDTO dto, @MappingTarget Recipe recipe);

    // converte a lista de RecipeTag pra TagDto list :)
    default List<TagDTO> recipeTagListToTagDtoList(List<RecipeTag> recipeTags) {
        if (recipeTags == null) return null;
        return recipeTags.stream()
                .filter(Objects::nonNull)
                .map(rt -> {
                    Tag t = rt.getTag();
                    if (t == null) return null;
                    TagDTO td = new TagDTO();
                    td.setId(t.getId());
                    td.setName(t.getName());
                    return td;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    default User userFromId(java.util.UUID id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    // converte a lista do CreateRecipeDto.ingredients para RecipeIngredient :)
    default List<RecipeIngredient> mapIngredientsFromCreateDto(List<RecipeIngredientCreateDTO> inputs) {
        if (inputs == null) return null;
        return inputs.stream().map(i -> {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setIngredient(new Ingredient());
            ri.getIngredient().setId(i.getIngredientId());
            ri.setQuantity(i.getQuantity());
            ri.setUnit(i.getUnit());
            ri.setNotes(i.getNotes());
            return ri;
        }).collect(Collectors.toList());
    }
}

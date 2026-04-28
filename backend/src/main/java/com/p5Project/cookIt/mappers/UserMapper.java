package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import org.mapstruct.*;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdRecipes", expression = "java(mapRecipeIds(user.getCreatedRecipes()))")
    @Mapping(target = "favoriteRecipes", expression = "java(mapRecipeIds(user.getFavoriteRecipes()))")
    @Mapping(target = "ratings", expression = "java(mapRatings(user.getRatings()))")
    UserDTO toDTO(User user);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    default List<String> mapRecipeIds(List<Recipe> recipes) {
        if (recipes == null) return null;
        return recipes.stream().map(Recipe::getId).collect(Collectors.toList());
    }

    default Map<String, Integer> mapRatings(Map<Recipe, Integer> ratings) {
        if (ratings == null) return null;
        return ratings.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue));
    }
}
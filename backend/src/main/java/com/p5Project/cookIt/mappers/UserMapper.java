package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdRecipes", expression = "java(mapCreatedRecipes(user.getCreatedRecipes()))")
    @Mapping(target = "favoriteRecipes", expression = "java(mapFavoriteRecipes(user.getFavoriteRecipes()))")
    @Mapping(target = "ratings", ignore = true)
    UserDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    default List<String> mapCreatedRecipes(List<Recipe> recipes) {
        if (recipes == null) return null;
        return recipes.stream().map(Recipe::getId).collect(Collectors.toList());
    }

    default List<String> mapFavoriteRecipes(List<Recipe> recipes) {
        if (recipes == null) return null;
        return recipes.stream().map(Recipe::getId).collect(Collectors.toList());
    }
}
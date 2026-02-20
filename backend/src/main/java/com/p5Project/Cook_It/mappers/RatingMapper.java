package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.rating.CreateRatingDTO;
import com.p5Project.Cook_It.models.dtos.rating.RatingDTO;
import com.p5Project.Cook_It.models.entities.Rating;
import com.p5Project.Cook_It.models.entities.Recipe;
import com.p5Project.Cook_It.models.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "recipeId", source = "recipe.id")
    RatingDTO toDto(Rating rating);

    @Mapping(target = "user", expression = "java(userFromId(dto.getUserId()))")
    @Mapping(target = "recipe", expression = "java(recipeFromId(dto.getRecipeId()))")
    Rating toEntity(CreateRatingDTO dto);

    default User userFromId(java.util.UUID id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Recipe recipeFromId(java.util.UUID id) {
        if (id == null) return null;
        Recipe r = new Recipe();
        r.setId(id);
        return r;
    }
}

package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.pantryItem.CreatePantryItemDTO;
import com.p5Project.Cook_It.models.dtos.pantryItem.PantryItemDTO;
import com.p5Project.Cook_It.models.entities.Ingredient;
import com.p5Project.Cook_It.models.entities.PantryItem;
import com.p5Project.Cook_It.models.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PantryItemMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "ingredientId", source = "ingredient.id")
    PantryItemDTO toDto(PantryItem entity);

    @Mapping(target = "user", expression = "java(userFromId(dto.getUserId()))")
    @Mapping(target = "ingredient", expression = "java(ingredientFromId(dto.getIngredientId()))")
    PantryItem toEntity(CreatePantryItemDTO dto);

    default User userFromId(java.util.UUID id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Ingredient ingredientFromId(java.util.UUID id) {
        if (id == null) return null;
        Ingredient i = new Ingredient();
        i.setId(id);
        return i;
    }
}
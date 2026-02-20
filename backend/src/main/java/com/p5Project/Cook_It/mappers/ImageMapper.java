package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.image.CreateImageDTO;
import com.p5Project.Cook_It.models.dtos.image.ImageDTO;
import com.p5Project.Cook_It.models.entities.Image;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO toDto(Image image);

    @Mapping(target = "recipe", expression = "java(recipeFromId(dto.getRecipeId()))")
    Image toEntity(CreateImageDTO dto);

    default Image recipeFromId(java.util.UUID id) {
        if (id == null) return null;
        Image img = new Image(); // se lembre que a associação recipe vai ser atribuída no service pq o mapper so mapeia campos simples :)
        return null;
    }
}
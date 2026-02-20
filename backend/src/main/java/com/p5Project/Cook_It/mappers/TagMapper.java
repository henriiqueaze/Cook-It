package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.tag.CreateTagDTO;
import com.p5Project.Cook_It.models.dtos.tag.TagDTO;
import com.p5Project.Cook_It.models.entities.Tag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toDto(Tag tag);

    Tag toEntity(CreateTagDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateTagDTO dto, @MappingTarget Tag entity);

    default Tag fromId(java.util.UUID id) {
        if (id == null) return null;
        Tag t = new Tag();
        t.setId(id);
        return t;
    }
}
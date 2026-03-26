package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.requests.UpdateCommentRequest;
import com.p5Project.cookIt.entities.Comment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.photo", target = "userPhoto")
    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toDTOList(List<Comment> comments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromRequest(UpdateCommentRequest request, @MappingTarget Comment comment);

}
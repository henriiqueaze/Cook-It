package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.requests.UpdateCommentRequest;
import com.p5Project.cookIt.entities.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.photo", target = "userPhoto")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt() != null ? comment.getCreatedAt().toString() : null)")
    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toDTOList(List<Comment> comments);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "text", source = "text")
    void updateCommentFromRequest(UpdateCommentRequest request, @MappingTarget Comment comment);
}
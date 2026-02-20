package com.p5Project.Cook_It.mappers;


import com.p5Project.Cook_It.models.dtos.comment.CommentDTO;
import com.p5Project.Cook_It.models.dtos.comment.CreateCommentDTO;
import com.p5Project.Cook_It.models.entities.Comment;
import com.p5Project.Cook_It.models.entities.Recipe;
import com.p5Project.Cook_It.models.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "recipeId", source = "recipe.id")
    CommentDTO toDto(Comment comment);

    @Mapping(target = "user", expression = "java(userFromId(dto.getUserId()))")
    @Mapping(target = "recipe", expression = "java(recipeFromId(dto.getRecipeId()))")
    Comment toEntity(CreateCommentDTO dto);

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

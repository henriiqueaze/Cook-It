package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Comment;
import com.p5Project.cookIt.models.entities.Recipe;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String photo;
    private List<Recipe> createdRecipes;
    private List<Recipe> favoriteRecipes;
    private List<Comment> comments;
}
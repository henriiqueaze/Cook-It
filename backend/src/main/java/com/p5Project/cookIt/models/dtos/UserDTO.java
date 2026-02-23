package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Comment;
import com.p5Project.cookIt.models.entities.PantryItem;
import com.p5Project.cookIt.models.entities.Rating;
import com.p5Project.cookIt.models.entities.Recipe;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String email;
    private String passwordHash;
    private String displayName;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Recipe> recipes;
    private List<Comment> comments;
    private List<Rating> ratings;
    private List<PantryItem> pantryItems;
}
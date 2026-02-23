package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {

    private UUID id;
    private User author;
    private String title;
    private String description;
    private String steps;
    private Integer servings;
    private Integer prepTimeMinutes;
    private Boolean published = false;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Comment> comments;
    private List<Rating> ratings;
    private List<Image> images;
    private List<RecipeIngredient> recipeIngredients;
    private List<RecipeTag> recipeTags;
}
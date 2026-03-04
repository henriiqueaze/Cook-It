package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.RecipeIngredient;
import com.p5Project.cookIt.models.entities.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO extends RepresentationModel<RecipeDTO> {

    private UUID id;
    private String title;
    private String image;
    private Integer prepTime;
    private Double rating;
    private Integer ratingsCount;
    private List<RecipeIngredient> ingredients;
    private String instructions;
    private User author;
    private String createdAt;
}
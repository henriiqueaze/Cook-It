package com.p5Project.cookIt.dtos;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDTO {

    private String id;

    private String name;

    private String image;

    private Integer prepTime;

    private Double rating;

    private Integer ratingsCount;

    private List<RecipeIngredientDTO> ingredients;

    private List<String> instructions;

    private String authorId;

    private String authorName;

    private String authorPhoto;

    private Integer avaliacaoUsuario;

    private String createdAt;
}
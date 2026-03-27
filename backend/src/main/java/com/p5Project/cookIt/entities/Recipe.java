package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String image;
    private Integer prepTime;
    private Double rating;
    private Integer ratingsCount;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients")
    private List<RecipeIngredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions")
    private List<String> instructions;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User author;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipe")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments;
}
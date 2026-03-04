package com.p5Project.cookIt.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="image", nullable = false)
    private String image;

    @Column(name="prepTime", nullable = false)
    private Integer prepTime;

    @Column(name="rating", nullable = false)
    private Double rating;

    @Column(name="ratingsCount")
    private Integer ratingsCount;

    @Column(name="ingredients", nullable = false)
    private List<RecipeIngredient> ingredients;

    @Column(name="instructions", nullable = false)
    private String instructions;

    @Column(name="author", nullable = false)
    private User author;

    @Column(name="createdAt", nullable = false)
    @CreationTimestamp
    private String createdAt;
}
package com.p5Project.cookIt.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String steps;

    private Integer servings;

    @Column(name = "prep_time_minutes")
    private Integer prepTimeMinutes;

    private Boolean published = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Relations
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<RecipeTag> recipeTags;
}
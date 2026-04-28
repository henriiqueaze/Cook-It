package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe extends AuditableEntity {

    private String name;
    private String description;
    private String image;
    private Integer prepTime;
    private Integer portions;
    private Double rating = 0.0;
    private Integer ratingsCount = 0;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients")
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_instructions")
    private List<String> instructions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User author;


    @OneToMany(mappedBy = "recipe")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments;

    public boolean isOwnedBy(String userId) {
        return author != null && Objects.equals(author.getId(), userId);
    }

    public Integer getUserRating(User user) {
        return user == null ? 0 : user.getRatingFor(this);
    }

    public Integer getPreviousUserRating(User user) {
        return user == null ? null : user.getStoredRatingFor(this);
    }

    public void replaceIngredients(List<RecipeIngredient> newIngredients) {
        ingredients = newIngredients == null ? new ArrayList<>() : new ArrayList<>(newIngredients);
    }

    public void replaceInstructions(List<String> newInstructions) {
        instructions = newInstructions == null ? new ArrayList<>() : new ArrayList<>(newInstructions);
    }

    public void registerRating(Integer previousRating, int newRating) {
        int currentCount = ratingsCount == null ? 0 : ratingsCount;
        double currentTotal = (rating == null ? 0.0 : rating) * currentCount;

        if (previousRating == null) {
            currentCount++;
        } else {
            currentTotal -= previousRating;
        }

        currentTotal += newRating;
        ratingsCount = currentCount;
        rating = currentCount == 0 ? 0.0 : currentTotal / currentCount;
    }

    public boolean matchesIngredients(List<String> normalizedIngredients) {
        if (normalizedIngredients == null || normalizedIngredients.isEmpty()) {
            return true;
        }

        return ingredients != null && ingredients.stream()
                .allMatch(recipeIngredient -> normalizedIngredients.contains(normalize(recipeIngredient.getIngredient())));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
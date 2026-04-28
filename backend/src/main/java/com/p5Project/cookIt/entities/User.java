package com.p5Project.cookIt.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String password;

    private String photo;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Recipe> createdRecipes;

    @ManyToMany
    @JoinTable(name = "favorite_recipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Recipe> favoriteRecipes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_ratings", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "recipe_id")
    @Column(name = "rating")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Map<Recipe, Integer> ratings = new HashMap<>();

    public boolean addFavoriteRecipe(Recipe recipe) {
        if (recipe == null || hasFavoriteRecipe(recipe)) {
            return false;
        }

        favoriteRecipes.add(recipe);
        return true;
    }

    public boolean removeFavoriteRecipe(Recipe recipe) {
        if (recipe == null || favoriteRecipes == null) {
            return false;
        }

        return favoriteRecipes.removeIf(existing -> sameRecipe(existing, recipe));
    }

    public boolean hasFavoriteRecipe(Recipe recipe) {
        if (recipe == null || favoriteRecipes == null) {
            return false;
        }

        return favoriteRecipes.stream().anyMatch(existing -> sameRecipe(existing, recipe));
    }

    public Integer getRatingFor(Recipe recipe) {
        return getStoredRatingFor(recipe) != null ? getStoredRatingFor(recipe) : 0;
    }

    public Integer getStoredRatingFor(Recipe recipe) {
        if (recipe == null || ratings == null) {
            return null;
        }

        return ratings.entrySet().stream()
                .filter(entry -> sameRecipe(entry.getKey(), recipe))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public void rateRecipe(Recipe recipe, int rating) {
        if (recipe == null) {
            return;
        }

        if (ratings == null) {
            ratings = new HashMap<>();
        }

        ratings.entrySet().removeIf(entry -> sameRecipe(entry.getKey(), recipe));
        ratings.put(recipe, rating);
    }

    private boolean sameRecipe(Recipe first, Recipe second) {
        return first != null && second != null && Objects.equals(first.getId(), second.getId());
    }
}
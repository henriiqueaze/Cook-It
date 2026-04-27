package com.p5Project.cookIt.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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
    private Map<Recipe, Integer> ratings;
}
package com.p5Project.cookIt.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "photo")
    private String photo;

    @OneToMany(mappedBy = "author")
    private List<Recipe> createdRecipes;

    private List<Recipe> favoriteRecipes;

    private Map<String, Integer> ratings;
}
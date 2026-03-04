package com.p5Project.cookIt.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="ingredient", nullable = false)
    private Ingredient ingredient;

    @Column(name="quantity", nullable = false)
    private Double quantity;

    @Column(name="unit", nullable = false)
    private String unit;
}
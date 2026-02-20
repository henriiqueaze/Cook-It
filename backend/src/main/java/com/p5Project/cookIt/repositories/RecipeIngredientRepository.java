package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, UUID> {
}

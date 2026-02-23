package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
}

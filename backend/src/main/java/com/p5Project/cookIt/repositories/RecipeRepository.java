package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
}

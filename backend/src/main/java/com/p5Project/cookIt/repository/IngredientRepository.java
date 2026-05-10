package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

    Optional<Ingredient> findByNameIgnoreCase(String name);

    List<Ingredient> findTop10ByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
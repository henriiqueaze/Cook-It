package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri WHERE LOWER(ri.ingredient) IN :ingredients")
    List<Recipe> findByIngredientNames(List<String> ingredients);

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.author WHERE r.id = :id")
    Optional<Recipe> findByIdWithDetails(String id);

    List<Recipe> findByAuthor_Id(String userId);}
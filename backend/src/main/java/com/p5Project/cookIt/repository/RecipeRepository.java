package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri WHERE LOWER(ri.ingredient) IN :ingredients")
    List<Recipe> findByIngredientNames(List<String> ingredients);

    List<Recipe> findByNameContainingIgnoreCase(String name);

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.author WHERE r.id = :id")
    Optional<Recipe> findByIdWithDetails(String id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorite_recipes WHERE recipe_id = :recipeId", nativeQuery = true)
    void deleteFromFavoritesByRecipeId(@Param("recipeId") String recipeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_ratings WHERE recipe_id = :recipeId", nativeQuery = true)
    void deleteFromUserRatingsByRecipeId(@Param("recipeId") String recipeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM recipes WHERE author_id = :authorId", nativeQuery = true)
    void deleteByAuthorId(@Param("authorId") String authorId);

    List<Recipe> findByAuthorId(String userId);

    List<Recipe> findTop5ByOrderByRatingDescRatingsCountDesc();
}

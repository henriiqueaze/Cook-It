package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.SearchRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.RecipeIngredientMapper;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public Page<RecipeDTO> getAllRecipes(Pageable pageable) {
        Page<Recipe> page = recipeRepository.findAll(pageable);

        return page.map(recipeMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public RecipeDTO getRecipe(String id, String userId) {
        Recipe recipe = recipeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));

        recipe.getIngredients().size();
        recipe.getInstructions().size();

        RecipeDTO dto = recipeMapper.toDTO(recipe);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            Integer avaliacaoUsuario = user.getRatings().entrySet().stream()
                    .filter(entry -> entry.getKey().getId().equals(recipe.getId()))
                    .map(java.util.Map.Entry::getValue)
                    .findFirst()
                    .orElse(0);

            dto.setAvaliacaoUsuario(avaliacaoUsuario);
        }

        return dto;
    }

    public RecipeDTO createRecipe(CreateRecipeRequest request, String userId) {
        User author = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));

        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setIngredients(request.getIngredients().stream().map(recipeIngredientMapper::toEntity).toList());

        recipe.setInstructions(request.getInstructions());
        recipe.setImage(request.getImage());
        recipe.setAuthor(author);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setRating(0.0);
        recipe.setRatingsCount(0);

        recipeRepository.save(recipe);

        return recipeMapper.toDTO(recipe);
    }

    public RecipeDTO updateRecipe(String id, UpdateRecipeRequest request) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        recipeMapper.updateRecipeFromRequest(request, recipe);
        recipeRepository.save(recipe);

        return recipeMapper.toDTO(recipe);
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteFromFavoritesByRecipeId(id);
        recipeRepository.deleteFromUserRatingsByRecipeId(id);
        recipeRepository.deleteById(id);
    }

    @Transactional
    public void rateRecipe(String recipeId, String userId, int rating) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Integer previousRating = user.getRatings().entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(recipeId))
                .map(java.util.Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        double total = recipe.getRating() * recipe.getRatingsCount();

        if (previousRating == null) {
            recipe.setRatingsCount(recipe.getRatingsCount() + 1);
        } else {
            total -= previousRating;
        }

        total += rating;
        recipe.setRating(total / recipe.getRatingsCount());

        user.getRatings().entrySet().removeIf(entry -> entry.getKey().getId().equals(recipeId));
        user.getRatings().put(recipe, rating);

        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public List<RecipeDTO> searchRecipes(SearchRecipeRequest request) {

        List<String> ingredients = request.getIngredients().stream().map(String::toLowerCase).toList();
        List<Recipe> recipes = recipeRepository.findByIngredientNames(ingredients);

        if (Boolean.TRUE.equals(request.getExactMatch())) {
            recipes = recipes.stream().filter(r -> r.getIngredients().stream().allMatch(i -> ingredients.contains(i.getIngredient().toLowerCase()))).toList();
        }

        recipes = sortRecipes(recipes, request.getSortBy());

        return recipes.stream().map(recipeMapper::toDTO).toList();
    }

    @Transactional
    public List<RecipeDTO> getUserRecipes(String userId) {
        return recipeMapper.toDTOList(recipeRepository.findByAuthorId(userId));
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> getTopRatedRecipes() {
        return recipeRepository.findTop5ByOrderByRatingDescRatingsCountDesc()
                .stream()
                .map(recipeMapper::toDTO)
                .toList();
    }

    private List<Recipe> sortRecipes(List<Recipe> recipes, String sortBy) {

        if (sortBy == null) return recipes;

        return switch (sortBy) {

            case "time" -> recipes.stream().sorted(Comparator.comparing(Recipe::getPrepTime)).toList();

            case "rating" -> recipes.stream().sorted(Comparator.comparing(Recipe::getRating).reversed()).toList();

            case "popular" -> recipes.stream().sorted(Comparator.comparing(Recipe::getRatingsCount).reversed()).toList();

            default -> recipes;
        };
    }
}
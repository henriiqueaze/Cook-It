package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.SearchRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.RecipeIngredient;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ForbiddenOperationException;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.RecipeIngredientMapper;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.repository.CommentRepository;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final IngredientService ingredientService;
    private final ModerationService moderationService;
    private final CloudinaryService cloudinaryService;
    private final CommentRepository commentRepository;

    public Page<RecipeDTO> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(recipeMapper::toDTO);
    }

    public List<RecipeDTO> getUserRecipes(String userId) {
        return recipeMapper.toDTOList(recipeRepository.findByAuthorId(userId));
    }

    public RecipeDTO getRecipe(String id, String userId) {
        Recipe recipe = findRecipeWithDetails(id);
        RecipeDTO dto = recipeMapper.toDTO(recipe);
        applyUserRating(dto, recipe, userId);
        return dto;
    }

    @Transactional
    public RecipeDTO createRecipe(CreateRecipeRequest request, String userId, MultipartFile image) {
        moderationService.ensureRecipeAllowed(request.name(), request.description(), request.ingredients(), request.instructions());
        Recipe recipe = buildRecipeFromCreateRequest(request, userId);
        applyImageIfPresent(recipe, image);
        recipeRepository.saveAndFlush(recipe);
        return recipeMapper.toDTO(recipe);
    }

    @Transactional
    public RecipeDTO updateRecipe(String id, UpdateRecipeRequest request, MultipartFile image, String requestingUserId) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));
        if (!recipe.isOwnedBy(requestingUserId)) {
            throw new ForbiddenOperationException("Você não tem permissão para editar esta receita");
        }
        moderationService.ensureRecipeAllowed(request.name(), request.description(), request.ingredients(), request.instructions());
        recipeMapper.updateRecipeFromRequest(request, recipe);
        updateIngredientsIfPresent(recipe, request.ingredients());
        applyImageIfPresent(recipe, image);
        recipeRepository.saveAndFlush(recipe);
        return recipeMapper.toDTO(recipe);
    }

    @Transactional
    public void deleteRecipe(String id) {
        commentRepository.deleteByRecipeId(id);
        recipeRepository.deleteFromFavoritesByRecipeId(id);
        recipeRepository.deleteFromUserRatingsByRecipeId(id);
        recipeRepository.deleteById(id);
    }

    @Transactional
    public void rateRecipe(String recipeId, String userId, int rating) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));
        User user = findUserById(userId);

        Integer previousRating = recipe.getPreviousUserRating(user);
        recipe.registerRating(previousRating, rating);
        user.rateRecipe(recipe, rating);

        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public List<RecipeDTO> searchRecipes(SearchRecipeRequest request) {
        List<String> normalizedIngredients = request.normalizedIngredients();
        String normalizedRecipeName = request.normalizedRecipeName();

        if (normalizedIngredients.isEmpty() && normalizedRecipeName.isBlank()) {
            return List.of();
        }

        List<Recipe> recipes = normalizedIngredients.isEmpty()
                ? recipeRepository.findByNameContainingIgnoreCase(normalizedRecipeName)
                : recipeRepository.findByIngredientNames(normalizedIngredients);

        if (request.exactMatch()) {
            recipes = filterExactMatch(recipes, normalizedIngredients);
        }

        if (!normalizedRecipeName.isBlank() && !normalizedIngredients.isEmpty()) {
            recipes = filterByRecipeName(recipes, normalizedRecipeName);
        }

        return sortRecipes(recipes, request.sortBy()).stream()
                .map(recipeMapper::toDTO)
                .toList();
    }

    public List<RecipeDTO> getTopRatedRecipes() {
        return recipeRepository.findTop5ByOrderByRatingDescRatingsCountDesc()
                .stream()
                .map(recipeMapper::toDTO)
                .toList();
    }

    private Recipe findRecipeWithDetails(String id) {
        Recipe recipe = recipeRepository.findByIdWithDetails(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));

        recipe.getIngredients().forEach(ignored -> {
        });
        recipe.getInstructions().forEach(ignored -> {
        });
        return recipe;
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    private Recipe buildRecipeFromCreateRequest(CreateRecipeRequest request, String userId) {
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setPrepTime(request.prepTime());
        recipe.setPortions(request.portions());
        recipe.replaceIngredients(buildRecipeIngredients(request.ingredients()));
        recipe.replaceInstructions(request.instructions());
        recipe.setAuthor(findUserById(userId));
        recipe.setRating(0.0);
        recipe.setRatingsCount(0);
        return recipe;
    }

    private void updateIngredientsIfPresent(Recipe recipe, List<RecipeIngredientDTO> ingredients) {
        if (ingredients != null) {
            recipe.replaceIngredients(buildRecipeIngredients(ingredients));
        }
    }

    private List<RecipeIngredient> buildRecipeIngredients(List<RecipeIngredientDTO> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        return ingredients.stream()
                .map(this::toRecipeIngredient)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private RecipeIngredient toRecipeIngredient(RecipeIngredientDTO ingredientDTO) {
        ingredientService.findOrCreate(ingredientDTO.ingredient());
        return recipeIngredientMapper.toEntity(ingredientDTO);
    }

    private void applyImageIfPresent(Recipe recipe, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            recipe.setImage(cloudinaryService.uploadImage(image, "cookit/recipes"));
        }
    }

    private void applyUserRating(RecipeDTO dto, Recipe recipe, String userId) {
        if (userId == null) {
            return;
        }

        User user = findUserById(userId);
        dto.setUserRating(recipe.getUserRating(user));
    }

    private List<Recipe> filterExactMatch(List<Recipe> recipes, List<String> ingredients) {
        return recipes.stream()
                .filter(recipe -> recipe.matchesIngredients(ingredients))
                .toList();
    }

    private List<Recipe> filterByRecipeName(List<Recipe> recipes, String normalizedRecipeName) {
        return recipes.stream()
                .filter(recipe -> recipe.getName() != null && recipe.getName().toLowerCase().contains(normalizedRecipeName))
                .toList();
    }

    private List<Recipe> sortRecipes(List<Recipe> recipes, String sortBy) {
        if (sortBy == null) {
            return recipes;
        }

        return switch (sortBy) {
            case "time" -> recipes.stream().sorted(Comparator.comparing(Recipe::getPrepTime)).toList();
            case "rating" -> recipes.stream().sorted(Comparator.comparing(Recipe::getRating).reversed()).toList();
            case "popular" -> recipes.stream().sorted(Comparator.comparing(Recipe::getRatingsCount).reversed()).toList();
            default -> recipes;
        };
    }
}

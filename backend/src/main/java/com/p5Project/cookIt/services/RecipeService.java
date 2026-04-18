package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.SearchRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.RecipeIngredient;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.RecipeIngredientMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final IngredientService ingredientService;
    private final CloudinaryService cloudinaryService;

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
        Recipe recipe = buildRecipeFromCreateRequest(request, userId);
        applyImageIfPresent(recipe, image);
        recipeRepository.saveAndFlush(recipe);
        return recipeMapper.toDTO(recipe);
    }

    @Transactional
    public RecipeDTO updateRecipe(String id, UpdateRecipeRequest request, MultipartFile image, String requestingUserId) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));
        if (!recipe.getAuthor().getId().equals(requestingUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar esta receita");
        }
        recipeMapper.updateRecipeFromRequest(request, recipe);
        updateIngredientsIfPresent(recipe, request.getIngredients());
        applyImageIfPresent(recipe, image);
        recipeRepository.saveAndFlush(recipe);
        return recipeMapper.toDTO(recipe);
    }

    @Transactional
    public void deleteRecipe(String id) {
        recipeRepository.deleteFromFavoritesByRecipeId(id);
        recipeRepository.deleteFromUserRatingsByRecipeId(id);
        recipeRepository.deleteById(id);
    }

    @Transactional
    public void rateRecipe(String recipeId, String userId, int rating) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));
        User user = findUserById(userId);

        Integer previousRating = findPreviousRating(user, recipeId);
        updateRecipeRating(recipe, previousRating, rating);
        updateUserRating(user, recipe, rating);

        userRepository.save(user);
        recipeRepository.save(recipe);
    }

    public List<RecipeDTO> searchRecipes(SearchRecipeRequest request) {
        List<String> normalizedIngredients = normalizeIngredients(request);
        List<Recipe> recipes = recipeRepository.findByIngredientNames(normalizedIngredients);

        if (Boolean.TRUE.equals(request.getExactMatch())) {
            recipes = filterExactMatch(recipes, normalizedIngredients);
        }

        return sortRecipes(recipes, request.getSortBy()).stream()
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

        recipe.getIngredients().size();
        recipe.getInstructions().size();
        return recipe;
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    private Recipe buildRecipeFromCreateRequest(CreateRecipeRequest request, String userId) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setPortions(request.getPortions());
        recipe.setIngredients(buildRecipeIngredients(request.getIngredients()));
        recipe.setInstructions(request.getInstructions());
        recipe.setAuthor(findUserById(userId));
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setRating(0.0);
        recipe.setRatingsCount(0);
        return recipe;
    }

    private void updateIngredientsIfPresent(Recipe recipe, List<RecipeIngredientDTO> ingredients) {
        if (ingredients != null) {
            recipe.setIngredients(buildRecipeIngredients(ingredients));
        }
    }

    private List<RecipeIngredient> buildRecipeIngredients(List<RecipeIngredientDTO> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }

        return ingredients.stream()
                .map(this::toRecipeIngredient)
                .toList();
    }

    private RecipeIngredient toRecipeIngredient(RecipeIngredientDTO ingredientDTO) {
        ingredientService.findOrCreate(ingredientDTO.getIngredient());
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
        dto.setUserRating(findUserRatingForRecipe(user, recipe.getId()));
    }

    private Integer findUserRatingForRecipe(User user, String recipeId) {
        return user.getRatings().entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(recipeId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);
    }

    private Integer findPreviousRating(User user, String recipeId) {
        return user.getRatings().entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(recipeId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private void updateRecipeRating(Recipe recipe, Integer previousRating, int newRating) {
        double total = recipe.getRating() * recipe.getRatingsCount();

        if (previousRating == null) {
            recipe.setRatingsCount(recipe.getRatingsCount() + 1);
        } else {
            total -= previousRating;
        }

        total += newRating;
        recipe.setRating(total / recipe.getRatingsCount());
    }

    private void updateUserRating(User user, Recipe recipe, int rating) {
        user.getRatings().entrySet().removeIf(entry -> entry.getKey().getId().equals(recipe.getId()));
        user.getRatings().put(recipe, rating);
    }

    private List<String> normalizeIngredients(SearchRecipeRequest request) {
        if (request.getIngredients() == null) {
            return List.of();
        }

        return request.getIngredients().stream()
                .filter(ingredient -> ingredient != null && !ingredient.isBlank())
                .map(String::toLowerCase)
                .toList();
    }

    private List<Recipe> filterExactMatch(List<Recipe> recipes, List<String> ingredients) {
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients().stream()
                        .allMatch(recipeIngredient -> ingredients.contains(recipeIngredient.getIngredient().toLowerCase())))
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
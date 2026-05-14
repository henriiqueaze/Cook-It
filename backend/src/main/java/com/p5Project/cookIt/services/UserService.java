package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.DeleteUserRequest;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.Ingredient;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.RecipeIngredient;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ForbiddenOperationException;
import com.p5Project.cookIt.exceptions.InvalidCredentialsException;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.CommentRepository;
import com.p5Project.cookIt.repository.IngredientRepository;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    
    private final CommentRepository commentRepository;
    private final IngredientRepository ingredientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public List<String> getFavorites(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"))
                .getFavoriteRecipes().stream()
                .map(Recipe::getId)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(String userId) {
        return userMapper.toDTO(findUserById(userId));
    }

    public void addFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));

        if (user.addFavoriteRecipe(recipe)) {
            userRepository.save(user);
        }
    }

    public void removeFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!"));

        if (user.removeFavoriteRecipe(recipe)) {
            userRepository.save(user);
        }
    }

    public UserDTO updateUser(String userId, UpdateUserRequest request, MultipartFile photo) {
        User user = findUserById(userId);
        userMapper.updateUserFromRequest(request, user);
        updatePhotoIfPresent(user, photo);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(String authenticatedUserId, String userId, UpdateUserRequest request, MultipartFile photo) {
        ensureCanUpdate(authenticatedUserId, userId);
        return updateUser(userId, request, photo);
    }

    public void deleteUser(String authenticatedUserId, String userId, DeleteUserRequest request) {
        validateDeletePassword(authenticatedUserId, userId, request);

        User user = findUserById(userId);

        commentRepository.deleteByUserId(userId);
        recipeRepository.deleteByAuthorId(userId);
        deleteOrphanIngredients();
        userRepository.delete(user);
    }

    public void validateDeletePassword(String authenticatedUserId, String userId, DeleteUserRequest request) {
        ensureCanDelete(authenticatedUserId, userId);

        User user = findUserById(userId);
        ensurePasswordMatches(request.password(), user.getPassword());
    }

    private void updatePhotoIfPresent(User user, MultipartFile photo) {
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = cloudinaryService.uploadImage(photo, "cookit/users");
            user.setPhoto(photoUrl);
        }
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    private void ensureCanUpdate(String authenticatedUserId, String userId) {
        if (!authenticatedUserId.equals(userId)) {
            throw new ForbiddenOperationException("You cannot update another user");
        }
    }

    private void ensureCanDelete(String authenticatedUserId, String userId) {
        if (!authenticatedUserId.equals(userId)) {
            throw new ForbiddenOperationException("You cannot delete another user");
        }
    }

    private void ensurePasswordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidCredentialsException("Invalid password");
        }
    }

    private void deleteOrphanIngredients() {
        Set<String> usedIngredients = recipeRepository.findAll().stream()
                .flatMap(recipe -> recipe.getIngredients() == null ? java.util.stream.Stream.empty() : recipe.getIngredients().stream())
                .map(RecipeIngredient::getIngredient)
                .filter(name -> name != null && !name.isBlank())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<Ingredient> orphanIngredients = ingredientRepository.findAll().stream()
                .filter(ingredient -> ingredient.getName() == null || !usedIngredients.contains(ingredient.getName().toLowerCase()))
                .toList();

        if (!orphanIngredients.isEmpty()) {
            ingredientRepository.deleteAllInBatch(orphanIngredients);
        }
    }
}

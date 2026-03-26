package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final UserMapper userMapper;

    public List<String> getFavorites(String userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getFavoriteRecipes().stream().map(Recipe::getId).toList();
    }

    public void addFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId).orElseThrow();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        if (!user.getFavoriteRecipes().contains(recipe)) {
            user.getFavoriteRecipes().add(recipe);
        }

        userRepository.save(user);
    }

    public void removeFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId).orElseThrow();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        user.getFavoriteRecipes().remove(recipe);

        userRepository.save(user);
    }

    public UserDTO updateUser(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateUserFromRequest(request, user);

        userRepository.save(user);

        return userMapper.toDTO(user);
    }
}

package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.UserControllerDocs;
import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import com.p5Project.cookIt.services.CommentService;
import com.p5Project.cookIt.services.RecipeService;
import com.p5Project.cookIt.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final RecipeService recipeService;
    private final CommentService commentService;

    @GetMapping("/favorites")
    @Override
    public List<String> getFavorites(@AuthenticationPrincipal UserPrincipal user) {
        return userService.getFavorites(user.getId());
    }

    @PostMapping("/favorites/{recipeId}")
    @Override
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserPrincipal user, @PathVariable String recipeId) {
        userService.addFavorite(user.getId(), recipeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{recipeId}")
    @Override
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal UserPrincipal user, @PathVariable String recipeId) {
        userService.removeFavorite(user.getId(), recipeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/recipes")
    @Override
    public List<RecipeDTO> getUserRecipes(@PathVariable String userId) {
        return recipeService.getUserRecipes(userId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public UserDTO updateUser(@PathVariable String id, @AuthenticationPrincipal UserPrincipal user, @RequestPart("data") UpdateUserRequest request, @RequestPart(value = "photo", required = false) MultipartFile photo) {
        if (!user.getId().equals(id)) {
            throw new RuntimeException("You cannot update another user");
        }

        return userService.updateUser(id, request, photo);
    }

    @GetMapping("/{userId}/comments")
    @Override
    public List<CommentDTO> getUserComments(@PathVariable String userId) {
        return commentService.getUserComments(userId);
    }
}
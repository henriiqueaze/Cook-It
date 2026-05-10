package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.RecipeControllerDocs;
import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.RateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.SearchRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import com.p5Project.cookIt.services.CommentService;
import com.p5Project.cookIt.services.RecipeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Receitas", description = "Gerenciamento de receitas")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recipes")
public class RecipeController implements RecipeControllerDocs {

    private final RecipeService recipeService;
    private final CommentService commentService;

    @GetMapping
    @Override
    public Page<RecipeDTO> getAll(Pageable pageable) {
        return recipeService.getAllRecipes(pageable);
    }

    @GetMapping("/{id}")
    @Override
    public RecipeDTO getRecipe(@PathVariable String id, @AuthenticationPrincipal UserPrincipal user) {
        return recipeService.getRecipe(id, user != null ? user.getId() : null);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public RecipeDTO create(@Valid @RequestPart("data") CreateRecipeRequest request, @RequestPart(value = "image", required = false) MultipartFile image, @AuthenticationPrincipal UserPrincipal user) {
        return recipeService.createRecipe(request, user.getId(), image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public RecipeDTO update(@PathVariable String id, @RequestPart("data") UpdateRecipeRequest request, @RequestPart(value = "image", required = false) MultipartFile image, @AuthenticationPrincipal UserPrincipal user) {
        return recipeService.updateRecipe(id, request, image, user.getId());
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable String id) {
        recipeService.deleteRecipe(id);
    }

    @PostMapping("/search")
    @Override
    public List<RecipeDTO> search(@RequestBody SearchRecipeRequest request) {
        return recipeService.searchRecipes(request);
    }

    @PostMapping("/{id}/rate")
    @Override
    public void rateRecipe(@PathVariable String id, @Valid @RequestBody RateRecipeRequest request, @AuthenticationPrincipal UserPrincipal user) {
        recipeService.rateRecipe(id, user.getId(), request.rating());
    }

    @GetMapping("/{recipeId}/comments")
    @Override
    public List<CommentDTO> getRecipeComments(@PathVariable String recipeId) {
        return commentService.getRecipeComments(recipeId);
    }

    @GetMapping("/top-rated")
    @Override
    public List<RecipeDTO> getTopRatedRecipes() {
        return recipeService.getTopRatedRecipes();
    }
}
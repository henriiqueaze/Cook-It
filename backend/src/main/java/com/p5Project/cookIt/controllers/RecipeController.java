package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO findRatingById(@PathVariable UUID id) {
        return service.findRecipeById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RecipeDTO> findAllRating() {
        return service.findAllRecipes();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO createRating(@RequestBody RecipeDTO recipe) {
        return service.createRecipe(recipe);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO updateRating(@RequestBody RecipeDTO recipe) {
        return service.updateRecipe(recipe);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO updateRatingField(@PathVariable UUID id, @RequestBody RecipeDTO recipe) {
        return service.updateRecipeField(id, recipe);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable UUID id) {
        service.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}

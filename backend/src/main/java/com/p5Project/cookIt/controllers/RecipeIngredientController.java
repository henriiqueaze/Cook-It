package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.services.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipeIngredient")
public class RecipeIngredientController {

    @Autowired
    private RecipeIngredientService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO findRecipeIngredientById(@PathVariable UUID id) {
        return service.findRecipeIngredientById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RecipeIngredientDTO> findAllRecipeIngredients() {
        return service.findAllRecipeIngredients();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO createRecipeIngredient(@RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.createRecipeIngredient(recipeIngredient);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO updateRecipeIngredient(@RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredient(recipeIngredient);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO updateRecipeIngredientField(@PathVariable UUID id, @RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredientField(id, recipeIngredient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeIngredient(@PathVariable UUID id) {
        service.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}

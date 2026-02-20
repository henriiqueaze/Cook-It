package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.services.RecipeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipeTag")
public class RecipeTagController {

    @Autowired
    private RecipeTagService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeTagDTO findRecipeTagById(@PathVariable UUID id) {
        return service.findRecipeTagById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RecipeTagDTO> findAllRecipeTags() {
        return service.findAllRecipeTags();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeTagDTO createRecipeTag(@RequestBody RecipeTagDTO recipeTag) {
        return service.createRecipeTag(recipeTag);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeTagDTO updateRecipeTag(@RequestBody RecipeTagDTO recipeTag) {
        return service.updateRecipeTag(recipeTag);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeTagDTO updateRecipeTagField(@PathVariable UUID id, @RequestBody RecipeTagDTO recipeTag) {
        return service.updateRecipeTagField(id, recipeTag);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id) {
        service.deleteRecipeTag(id);
        return ResponseEntity.noContent().build();
    }
}

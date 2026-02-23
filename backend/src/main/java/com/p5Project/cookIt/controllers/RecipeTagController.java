package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.services.RecipeTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipeTag")
@Tag(name = "RecipeTag", description = "Endpoints para gerenciamento de tags vinculadas às receitas")
public class RecipeTagController implements com.p5Project.cookIt.controllers.docs.RecipeTagControllerDocs {

    @Autowired
    private RecipeTagService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<RecipeTagDTO> findRecipeTagById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findRecipeTagById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<List<RecipeTagDTO>> findAllRecipeTags() {
        return ResponseEntity.ok(service.findAllRecipeTags());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<RecipeTagDTO> createRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.createRecipeTag(recipeTag));
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<RecipeTagDTO> updateRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.updateRecipeTag(recipeTag));
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<RecipeTagDTO> updateRecipeTagField(@PathVariable UUID id,
                                                             @org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.updateRecipeTagField(id, recipeTag));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id) {
        service.deleteRecipeTag(id);
        return ResponseEntity.noContent().build();
    }
}
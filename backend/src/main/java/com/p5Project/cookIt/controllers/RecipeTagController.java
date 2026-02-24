package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.RecipeTagControllerDocs;
import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.services.RecipeTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recipeTag")
@Tag(name = "RecipeTag", description = "Endpoints para gerenciamento de tags vinculadas às receitas")
public class RecipeTagController implements RecipeTagControllerDocs {

    @Autowired
    private RecipeTagService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<RecipeTagDTO> findRecipeTagById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findRecipeTagById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<RecipeTagDTO>>> findAllRecipeTags(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAllRecipeTags(pageable));
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
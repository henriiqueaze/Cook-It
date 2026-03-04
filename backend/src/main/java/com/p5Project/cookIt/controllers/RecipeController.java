package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.RecipeControllerDocs;
import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.services.RecipeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/recipes")
public class RecipeController implements RecipeControllerDocs {

    @Autowired
    private RecipeService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO findRecipeById(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        return service.findRecipeById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<RecipeDTO>>> findAllRecipes(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return ResponseEntity.ok(service.findAllRecipes(pageable));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO createRecipe(@RequestBody(description = "Objeto contendo os dados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.createRecipe(recipe);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO updateRecipe(@RequestBody(description = "Objeto contendo os dados atualizados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipe(recipe);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO updateRecipeField(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id,
                                       @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipeField(id, recipe);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteRecipe(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        service.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
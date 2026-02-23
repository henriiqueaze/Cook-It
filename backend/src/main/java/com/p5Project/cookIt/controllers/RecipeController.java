package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipe")
@Tag(name = "Recipe", description = "Endpoints para gerenciamento de receitas")
public class RecipeController implements com.p5Project.cookIt.controllers.docs.RecipeControllerDocs {

    @Autowired
    private RecipeService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO findRatingById(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        return service.findRecipeById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<RecipeDTO> findAllRating() {
        return service.findAllRecipes();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO createRating(@RequestBody(description = "Objeto contendo os dados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.createRecipe(recipe);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO updateRating(@RequestBody(description = "Objeto contendo os dados atualizados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipe(recipe);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeDTO updateRatingField(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id,
                                       @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipeField(id, recipe);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteRating(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        service.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
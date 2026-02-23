package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.services.RecipeIngredientService;
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
@RequestMapping("/recipeIngredient")
@Tag(name = "Recipe Ingredient", description = "Endpoints para gerenciamento da relação entre receitas e ingredientes")
public class RecipeIngredientController implements com.p5Project.cookIt.controllers.docs.RecipeIngredientControllerDocs {

    @Autowired
    private RecipeIngredientService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeIngredientDTO findRecipeIngredientById(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id) {
        return service.findRecipeIngredientById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<RecipeIngredientDTO> findAllRecipeIngredients() {
        return service.findAllRecipeIngredients();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeIngredientDTO createRecipeIngredient(@RequestBody(description = "Objeto contendo os dados da relação receita-ingrediente", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.createRecipeIngredient(recipeIngredient);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeIngredientDTO updateRecipeIngredient(@RequestBody(description = "Objeto contendo os dados atualizados da relação", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredient(recipeIngredient);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RecipeIngredientDTO updateRecipeIngredientField(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id,
                                                           @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredientField(id, recipeIngredient);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteRecipeIngredient(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id) {
        service.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
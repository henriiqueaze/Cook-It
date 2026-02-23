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
public class RecipeIngredientController {

    @Autowired
    private RecipeIngredientService service;

    @Operation(summary = "Buscar relação receita-ingrediente por ID", description = "Retorna uma relação específica entre receita e ingrediente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação encontrada", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO findRecipeIngredientById(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id) {
        return service.findRecipeIngredientById(id);
    }

    @Operation(summary = "Listar todas as relações receita-ingrediente", description = "Retorna uma lista com todas as relações cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RecipeIngredientDTO> findAllRecipeIngredients() {
        return service.findAllRecipeIngredients();
    }

    @Operation(summary = "Criar nova relação receita-ingrediente", description = "Cria uma nova associação entre receita e ingrediente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação criada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO createRecipeIngredient(@RequestBody(description = "Objeto contendo os dados da relação receita-ingrediente", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.createRecipeIngredient(recipeIngredient);
    }

    @Operation(summary = "Atualizar relação receita-ingrediente completa", description = "Atualiza todas as informações de uma relação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação atualizada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO updateRecipeIngredient(@RequestBody(description = "Objeto contendo os dados atualizados da relação", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredient(recipeIngredient);
    }

    @Operation(summary = "Atualizar campo específico da receita-ingrediente", description = "Atualiza apenas campos específicos de uma relação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeIngredientDTO updateRecipeIngredientField(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id,
                                                           @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient) {
        return service.updateRecipeIngredientField(id, recipeIngredient);
    }

    @Operation(summary = "Deletar relação receita-ingrediente", description = "Remove uma relação entre receita e ingrediente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeIngredient(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id) {
        service.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
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
public class RecipeController {

    @Autowired
    private RecipeService service;

    @Operation(summary = "Buscar receita por ID", description = "Retorna uma receita específica com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita encontrada", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO findRatingById(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        return service.findRecipeById(id);
    }

    @Operation(summary = "Listar todas as receitas", description = "Retorna uma lista com todas as receitas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RecipeDTO> findAllRating() {
        return service.findAllRecipes();
    }

    @Operation(summary = "Criar nova receita", description = "Cria uma nova receita no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita criada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO createRating(@RequestBody(description = "Objeto contendo os dados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.createRecipe(recipe);
    }

    @Operation(summary = "Atualizar receita completa", description = "Atualiza todas as informações de uma receita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO updateRating(@RequestBody(description = "Objeto contendo os dados atualizados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipe(recipe);
    }

    @Operation(summary = "Atualizar campo específico da receita", description = "Atualiza apenas campos específicos de uma receita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RecipeDTO updateRatingField(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id,
                                       @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe) {
        return service.updateRecipeField(id, recipe);
    }

    @Operation(summary = "Deletar receita", description = "Remove uma receita do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Receita deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRating(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id) {
        service.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
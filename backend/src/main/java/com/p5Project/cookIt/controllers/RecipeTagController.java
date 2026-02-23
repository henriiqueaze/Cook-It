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
public class RecipeTagController {

    @Autowired
    private RecipeTagService service;

    @Operation(summary = "Buscar RecipeTag por ID", description = "Retorna uma RecipeTag específica com base no UUID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<RecipeTagDTO> findRecipeTagById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findRecipeTagById(id));
    }

    @Operation(summary = "Listar todas as RecipeTags", description = "Retorna todas as tags cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de RecipeTags retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<List<RecipeTagDTO>> findAllRecipeTags() {
        return ResponseEntity.ok(service.findAllRecipeTags());
    }

    @Operation(summary = "Criar RecipeTag", description = "Cria uma nova tag e associa a uma receita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<RecipeTagDTO> createRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.createRecipeTag(recipeTag));
    }

    @Operation(summary = "Atualizar RecipeTag", description = "Atualiza completamente uma tag existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<RecipeTagDTO> updateRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.updateRecipeTag(recipeTag));
    }

    @Operation(summary = "Atualizar campo específico da RecipeTag", description = "Atualiza parcialmente uma tag existente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<RecipeTagDTO> updateRecipeTagField(@PathVariable UUID id,
                                                             @org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag) {
        return ResponseEntity.ok(service.updateRecipeTagField(id, recipeTag));
    }

    @Operation(summary = "Deletar RecipeTag", description = "Remove uma tag com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "RecipeTag removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id) {
        service.deleteRecipeTag(id);
        return ResponseEntity.noContent().build();
    }
}
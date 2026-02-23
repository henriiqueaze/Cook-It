package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface RecipeTagControllerDocs {
    @Operation(summary = "Buscar RecipeTag por ID", description = "Retorna uma RecipeTag específica com base no UUID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    ResponseEntity<RecipeTagDTO> findRecipeTagById(@PathVariable UUID id);

    @Operation(summary = "Listar todas as RecipeTags", description = "Retorna todas as tags cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de RecipeTags retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class)))
    })
    ResponseEntity<List<RecipeTagDTO>> findAllRecipeTags();

    @Operation(summary = "Criar RecipeTag", description = "Cria uma nova tag e associa a uma receita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<RecipeTagDTO> createRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag);

    @Operation(summary = "Atualizar RecipeTag", description = "Atualiza completamente uma tag existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    ResponseEntity<RecipeTagDTO> updateRecipeTag(@org.springframework.web.bind.annotation.RequestBody RecipeTagDTO recipeTag);

    @Operation(summary = "Atualizar campo específico da RecipeTag", description = "Atualiza parcialmente uma tag existente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RecipeTag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    ResponseEntity<RecipeTagDTO> updateRecipeTagField(@PathVariable UUID id,
                                                      @RequestBody RecipeTagDTO recipeTag);

    @Operation(summary = "Deletar RecipeTag", description = "Remove uma tag com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "RecipeTag removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "RecipeTag não encontrada", content = @Content)
    })
    ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id);
}

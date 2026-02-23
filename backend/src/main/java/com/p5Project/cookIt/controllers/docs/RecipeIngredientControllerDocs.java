package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface RecipeIngredientControllerDocs {
    @Operation(summary = "Buscar relação receita-ingrediente por ID", description = "Retorna uma relação específica entre receita e ingrediente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação encontrada", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    RecipeIngredientDTO findRecipeIngredientById(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id);

    @Operation(summary = "Listar todas as relações receita-ingrediente", description = "Retorna uma lista com todas as relações cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class)))
    })
    List<RecipeIngredientDTO> findAllRecipeIngredients();

    @Operation(summary = "Criar nova relação receita-ingrediente", description = "Cria uma nova associação entre receita e ingrediente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação criada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    RecipeIngredientDTO createRecipeIngredient(@RequestBody(description = "Objeto contendo os dados da relação receita-ingrediente", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient);

    @Operation(summary = "Atualizar relação receita-ingrediente completa", description = "Atualiza todas as informações de uma relação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação atualizada com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    RecipeIngredientDTO updateRecipeIngredient(@RequestBody(description = "Objeto contendo os dados atualizados da relação", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient);

    @Operation(summary = "Atualizar campo específico da receita-ingrediente", description = "Atualiza apenas campos específicos de uma relação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    RecipeIngredientDTO updateRecipeIngredientField(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id,
                                                    @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeIngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeIngredientDTO recipeIngredient);

    @Operation(summary = "Deletar relação receita-ingrediente", description = "Remove uma relação entre receita e ingrediente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Relação não encontrada", content = @Content)
    })
    ResponseEntity<?> deleteRecipeIngredient(@Parameter(description = "ID da relação receita-ingrediente", required = true) @PathVariable UUID id);
}

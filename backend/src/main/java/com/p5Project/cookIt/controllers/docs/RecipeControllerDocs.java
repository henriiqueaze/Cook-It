package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.RecipeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface RecipeControllerDocs {
    @Operation(summary = "Buscar receita por ID", description = "Retorna uma receita específica com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita encontrada", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    RecipeDTO findRecipeById(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id);

    @Operation(summary = "Listar todas as receitas", description = "Retorna uma lista com todas as receitas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    ResponseEntity<PagedModel<EntityModel<RecipeDTO>>> findAllRecipes(Integer page, Integer size, String direction);

    @Operation(summary = "Criar nova receita", description = "Cria uma nova receita no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita criada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    RecipeDTO createRecipe(@RequestBody(description = "Objeto contendo os dados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe);

    @Operation(summary = "Atualizar receita completa", description = "Atualiza todas as informações de uma receita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    RecipeDTO updateRecipe(@RequestBody(description = "Objeto contendo os dados atualizados da receita", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe);

    @Operation(summary = "Atualizar campo específico da receita", description = "Atualiza apenas campos específicos de uma receita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    RecipeDTO updateRecipeField(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id,
                                @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RecipeDTO.class))) @org.springframework.web.bind.annotation.RequestBody RecipeDTO recipe);

    @Operation(summary = "Deletar receita", description = "Remove uma receita do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Receita deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    ResponseEntity<?> deleteRecipe(@Parameter(description = "ID da receita", required = true) @PathVariable UUID id);
}

package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.RateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.SearchRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeControllerDocs {
    @Operation(summary = "Listar receitas", description = "Retorna receitas paginadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    Page<RecipeDTO> getAll(Pageable pageable);

    @Operation(summary = "Buscar receita por ID", description = "Retorna uma receita específica com base no ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita encontrada",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    RecipeDTO getRecipe(@PathVariable String id, @AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Criar receita", description = "Cria uma nova receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita criada",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    RecipeDTO create(@Valid @RequestPart("data") CreateRecipeRequest request,
                     @RequestPart(value = "image", required = false) MultipartFile image,
                     @AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Atualizar receita", description = "Atualiza uma receita existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita atualizada",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    RecipeDTO update(@PathVariable String id,
                     @RequestPart("data") UpdateRecipeRequest request,
                     @RequestPart(value = "image", required = false) MultipartFile image,
                     @AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Excluir receita", description = "Remove uma receita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receita removida"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    void delete(@PathVariable String id);

    @Operation(summary = "Buscar receitas", description = "Busca receitas por filtros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados encontrados",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    List<RecipeDTO> search(@RequestBody SearchRecipeRequest request);

    @Operation(summary = "Avaliar receita", description = "Adiciona uma avaliação a uma receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação registrada"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    void rateRecipe(@PathVariable String id,
                    @Valid @RequestBody RateRecipeRequest request,
                    @AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Comentários da receita", description = "Lista comentários de uma receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentários retornados",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    })
    @GetMapping("/{recipeId}/comments")
    List<CommentDTO> getRecipeComments(@PathVariable String recipeId);

    @GetMapping("/top-rated")
    public List<RecipeDTO> getTopRatedRecipes();
}

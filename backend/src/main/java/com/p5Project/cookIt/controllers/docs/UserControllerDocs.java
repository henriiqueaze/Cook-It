package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserControllerDocs {
    @Operation(summary = "Listar favoritos", description = "Retorna receitas favoritas do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada")
    })
    List<String> getFavorites(@AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Adicionar favorito", description = "Adiciona uma receita aos favoritos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita adicionada aos favoritos")
    })
    ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserPrincipal user,
                                     @PathVariable String recipeId);

    @Operation(summary = "Remover favorito", description = "Remove receita dos favoritos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receita removida")
    })
    ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal UserPrincipal user,
                                        @PathVariable String recipeId);

    @Operation(summary = "Receitas do usuário", description = "Lista receitas criadas pelo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receitas retornadas",
                    content = @Content(schema = @Schema(implementation = RecipeDTO.class)))
    })
    List<RecipeDTO> getUserRecipes(@PathVariable String userId);

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    UserDTO updateUser(@PathVariable String id,
                       @AuthenticationPrincipal UserPrincipal user,
                       @RequestBody UpdateUserRequest request);

    @Operation(summary = "Comentários do usuário", description = "Lista comentários feitos pelo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentários retornados",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    })
    List<CommentDTO> getUserComments(@PathVariable String userId);
}

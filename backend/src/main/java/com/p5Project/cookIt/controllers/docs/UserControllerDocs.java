package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.models.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface UserControllerDocs {
    @Operation(summary = "Buscar User por ID", description = "Retorna um usuário específico com base no UUID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    ResponseEntity<UserDTO> findUserById(@PathVariable UUID id);

    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)))
    })
    ResponseEntity<PagedModel<EntityModel<UserDTO>>> findAllUsers(Integer page, Integer size, String direction);

    @Operation(summary = "Criar User", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user);

    @Operation(summary = "Atualizar User", description = "Atualiza completamente um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user);

    @Operation(summary = "Atualizar campo específico do User", description = "Atualiza parcialmente um usuário existente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    ResponseEntity<UserDTO> updateUserField(@PathVariable UUID id, @RequestBody UserDTO tag);

    @Operation(summary = "Deletar User", description = "Remove um usuário com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    ResponseEntity<?> deleteUser(@PathVariable UUID id);
}

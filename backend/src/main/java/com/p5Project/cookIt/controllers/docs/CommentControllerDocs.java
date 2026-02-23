package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.CommentDTO;
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

public interface CommentControllerDocs {

    @Operation(summary = "Buscar comentário por ID", description = "Retorna um comentário específico com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    CommentDTO findCommentById(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id);

    @Operation(summary = "Listar todos os comentários", description = "Retorna uma lista com todos os comentários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    })
    List<CommentDTO> findAllComments();

    @Operation(summary = "Criar novo comentário", description = "Cria um novo comentário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    CommentDTO createComment(@RequestBody(description = "Objeto contendo os dados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment);

    @Operation(summary = "Atualizar comentário completo", description = "Atualiza todas as informações de um comentário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    CommentDTO updateComment(@RequestBody(description = "Objeto contendo os dados atualizados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment);

    @Operation(summary = "Atualizar campo específico do comentário", description = "Atualiza apenas campos específicos de um comentário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado parcialmente com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    CommentDTO updateCommentField(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id,
                                  @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment);

    @Operation(summary = "Deletar comentário", description = "Remove um comentário do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comentário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    ResponseEntity<?> deleteComment(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id);
}

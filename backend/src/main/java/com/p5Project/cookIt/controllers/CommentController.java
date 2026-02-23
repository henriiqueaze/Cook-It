package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.services.CommentService;
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
@RequestMapping("/comment")
@Tag(name = "Comment", description = "Endpoints para gerenciamento de comentários das receitas")
public class CommentController {

    @Autowired
    private CommentService service;

    @Operation(summary = "Buscar comentário por ID", description = "Retorna um comentário específico com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO findCommentById(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id) {
        return service.findCommentById(id);
    }

    @Operation(summary = "Listar todos os comentários", description = "Retorna uma lista com todos os comentários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<CommentDTO> findAllComments() {
        return service.findAllComments();
    }

    @Operation(summary = "Criar novo comentário", description = "Cria um novo comentário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO createComment(@RequestBody(description = "Objeto contendo os dados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.createComment(comment);
    }

    @Operation(summary = "Atualizar comentário completo", description = "Atualiza todas as informações de um comentário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO updateComment(@RequestBody(description = "Objeto contendo os dados atualizados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.updateComment(comment);
    }

    @Operation(summary = "Atualizar campo específico do comentário", description = "Atualiza apenas campos específicos de um comentário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado parcialmente com sucesso", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO updateCommentField(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id,
                                         @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.updateCommentField(id, comment);
    }

    @Operation(summary = "Deletar comentário", description = "Remove um comentário do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comentário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteComment(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id) {
        service.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
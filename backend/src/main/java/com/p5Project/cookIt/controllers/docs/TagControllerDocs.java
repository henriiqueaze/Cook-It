package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.TagDTO;
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

public interface TagControllerDocs {
    @Operation(summary = "Buscar Tag por ID", description = "Retorna uma tag específica com base no UUID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    ResponseEntity<TagDTO> findTagById(@PathVariable UUID id);

    @Operation(summary = "Listar todas as Tags", description = "Retorna todas as tags cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tags retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class)))
    })
    ResponseEntity<List<TagDTO>> findAllTags();

    @Operation(summary = "Criar Tag", description = "Cria uma nova tag no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tag);

    @Operation(summary = "Atualizar Tag", description = "Atualiza completamente uma tag existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    ResponseEntity<TagDTO> updateTag(@RequestBody TagDTO tag);

    @Operation(summary = "Atualizar campo específico da Tag", description = "Atualiza parcialmente uma tag existente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    ResponseEntity<TagDTO> updateTagField(@PathVariable UUID id, @RequestBody TagDTO tag);

    @Operation(summary = "Deletar Tag", description = "Remove uma tag com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id);
}

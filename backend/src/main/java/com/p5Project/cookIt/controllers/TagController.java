package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.TagDTO;
import com.p5Project.cookIt.services.TagService;
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
@RequestMapping("/tag")
@Tag(name = "Tag", description = "Endpoints para gerenciamento de tags")
public class TagController {

    @Autowired
    private TagService service;

    @Operation(summary = "Buscar Tag por ID", description = "Retorna uma tag específica com base no UUID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<TagDTO> findTagById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findTagById(id));
    }

    @Operation(summary = "Listar todas as Tags", description = "Retorna todas as tags cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tags retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<List<TagDTO>> findAllTags() {
        return ResponseEntity.ok(service.findAllTags());
    }

    @Operation(summary = "Criar Tag", description = "Cria uma nova tag no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tag) {
        return ResponseEntity.ok(service.createTag(tag));
    }

    @Operation(summary = "Atualizar Tag", description = "Atualiza completamente uma tag existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<TagDTO> updateTag(@RequestBody TagDTO tag) {
        return ResponseEntity.ok(service.updateTag(tag));
    }

    @Operation(summary = "Atualizar campo específico da Tag", description = "Atualiza parcialmente uma tag existente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<TagDTO> updateTagField(@PathVariable UUID id, @RequestBody TagDTO tag) {
        return ResponseEntity.ok(service.updateTagField(id, tag));
    }

    @Operation(summary = "Deletar Tag", description = "Remove uma tag com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id) {
        service.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
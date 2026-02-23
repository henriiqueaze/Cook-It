package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.services.RatingService;
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
@RequestMapping("/rating")
@Tag(name = "Rating", description = "Endpoints para gerenciamento de avaliações das receitas")
public class RatingController {

    @Autowired
    private RatingService service;

    @Operation(summary = "Buscar avaliação por ID", description = "Retorna uma avaliação específica com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada", content = @Content(schema = @Schema(implementation = RatingDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO findRatingById(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id) {
        return service.findRatingById(id);
    }

    @Operation(summary = "Listar todas as avaliações", description = "Retorna uma lista com todas as avaliações cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = RatingDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RatingDTO> findAllRating() {
        return service.findAllRatings();
    }

    @Operation(summary = "Criar nova avaliação", description = "Cria uma nova avaliação no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação criada com sucesso", content = @Content(schema = @Schema(implementation = RatingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO createRating(@RequestBody(description = "Objeto contendo os dados da avaliação", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.createRating(rating);
    }

    @Operation(summary = "Atualizar avaliação completa", description = "Atualiza todas as informações de uma avaliação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso", content = @Content(schema = @Schema(implementation = RatingDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO updateRating(@RequestBody(description = "Objeto contendo os dados atualizados da avaliação", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.updateRating(rating);
    }

    @Operation(summary = "Atualizar campo específico da avaliação", description = "Atualiza apenas campos específicos de uma avaliação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = RatingDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO updateRatingField(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id,
                                       @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.updateRatingField(id, rating);
    }

    @Operation(summary = "Deletar avaliação", description = "Remove uma avaliação do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avaliação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRating(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id) {
        service.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
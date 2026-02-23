package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.IngredientDTO;
import com.p5Project.cookIt.services.IngredientService;
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
@RequestMapping("/ingredient")
@Tag(name = "Ingredient", description = "Endpoints para gerenciamento de ingredientes")
public class IngredientController {

    @Autowired
    private IngredientService service;

    @Operation(summary = "Buscar ingrediente por ID", description = "Retorna um ingrediente específico com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrediente encontrado", content = @Content(schema = @Schema(implementation = IngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public IngredientDTO findIngredientById(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id) {
        return service.findIngredientById(id);
    }

    @Operation(summary = "Listar todos os ingredientes", description = "Retorna uma lista com todos os ingredientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = IngredientDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<IngredientDTO> findAllIngredients() {
        return service.findAllIngredients();
    }

    @Operation(summary = "Criar novo ingrediente", description = "Cria um novo ingrediente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrediente criado com sucesso", content = @Content(schema = @Schema(implementation = IngredientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public IngredientDTO createIngredient(@RequestBody(description = "Objeto contendo os dados do ingrediente", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.createIngredient(ingredient);
    }

    @Operation(summary = "Atualizar ingrediente completo", description = "Atualiza todas as informações de um ingrediente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrediente atualizado com sucesso", content = @Content(schema = @Schema(implementation = IngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado", content = @Content)
    })
    @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public IngredientDTO updateIngredient(@RequestBody(description = "Objeto contendo os dados atualizados do ingrediente", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.updateIngredient(ingredient);
    }

    @Operation(summary = "Atualizar campo específico do ingrediente", description = "Atualiza apenas campos específicos de um ingrediente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrediente atualizado parcialmente com sucesso", content = @Content(schema = @Schema(implementation = IngredientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public IngredientDTO updateIngredientField(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id,
                                               @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.updateIngredientField(id, ingredient);
    }

    @Operation(summary = "Deletar ingrediente", description = "Remove um ingrediente do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ingrediente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id) {
        service.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
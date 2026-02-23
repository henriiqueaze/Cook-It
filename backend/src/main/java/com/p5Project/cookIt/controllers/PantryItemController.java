package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import com.p5Project.cookIt.services.PantryItemService;
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
@RequestMapping("/pantryItem")
@Tag(name = "Pantry Item", description = "Endpoints para gerenciamento de itens da despensa do usuário")
public class PantryItemController {

    @Autowired
    private PantryItemService service;

    @Operation(summary = "Buscar item da despensa por ID", description = "Retorna um item da despensa específico com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO findPantryItemById(@Parameter(description = "ID do item", required = true) @PathVariable UUID id) {
        return service.findPantryItemById(id);
    }

    @Operation(summary = "Listar todos os itens da despensa", description = "Retorna uma lista com todos os itens da despensa cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<PantryItemDTO> findAllPantryItems() {
        return service.findAllPantryItems();
    }

    @Operation(summary = "Criar novo item da despensa", description = "Cria um novo item na despensa do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item criado com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO createPantryItem(@RequestBody(description = "Objeto contendo os dados do item da despensa", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.createPantryItem(comment);
    }

    @Operation(summary = "Atualizar item completo da despensa", description = "Atualiza todas as informações de um item existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO updatePantryItem(@RequestBody(description = "Objeto contendo os dados atualizados do item", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.updatePantryItem(comment);
    }

    @Operation(summary = "Atualizar campo específico da despensa", description = "Atualiza apenas campos específicos de um item existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado parcialmente com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO updatePantryItemField(@Parameter(description = "ID do item", required = true) @PathVariable UUID id,
                                               @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.updatePantryItemField(id, comment);
    }

    @Operation(summary = "Deletar item da despensa", description = "Remove um item da despensa com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePantryItem(@Parameter(description = "ID do item", required = true) @PathVariable UUID id) {
        service.deletePantryItem(id);
        return ResponseEntity.noContent().build();
    }
}
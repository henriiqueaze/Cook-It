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
public class PantryItemController implements com.p5Project.cookIt.controllers.docs.PantryItemControllerDocs {

    @Autowired
    private PantryItemService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PantryItemDTO findPantryItemById(@Parameter(description = "ID do item", required = true) @PathVariable UUID id) {
        return service.findPantryItemById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<PantryItemDTO> findAllPantryItems() {
        return service.findAllPantryItems();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PantryItemDTO createPantryItem(@RequestBody(description = "Objeto contendo os dados do item da despensa", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.createPantryItem(comment);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PantryItemDTO updatePantryItem(@RequestBody(description = "Objeto contendo os dados atualizados do item", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.updatePantryItem(comment);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PantryItemDTO updatePantryItemField(@Parameter(description = "ID do item", required = true) @PathVariable UUID id,
                                               @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment) {
        return service.updatePantryItemField(id, comment);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deletePantryItem(@Parameter(description = "ID do item", required = true) @PathVariable UUID id) {
        service.deletePantryItem(id);
        return ResponseEntity.noContent().build();
    }
}
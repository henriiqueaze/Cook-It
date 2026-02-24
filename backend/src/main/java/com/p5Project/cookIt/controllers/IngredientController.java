package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.IngredientControllerDocs;
import com.p5Project.cookIt.models.dtos.IngredientDTO;
import com.p5Project.cookIt.services.IngredientService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ingredient", description = "Endpoints para gerenciamento de ingredientes")
public class IngredientController implements IngredientControllerDocs {

    @Autowired
    private IngredientService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public IngredientDTO findIngredientById(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id) {
        return service.findIngredientById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<IngredientDTO>>> findAllIngredients(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAllIngredients(pageable));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public IngredientDTO createIngredient(@RequestBody(description = "Objeto contendo os dados do ingrediente", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.createIngredient(ingredient);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public IngredientDTO updateIngredient(@RequestBody(description = "Objeto contendo os dados atualizados do ingrediente", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.updateIngredient(ingredient);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public IngredientDTO updateIngredientField(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id,
                                               @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = IngredientDTO.class))) @org.springframework.web.bind.annotation.RequestBody IngredientDTO ingredient) {
        return service.updateIngredientField(id, ingredient);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> deleteComment(@Parameter(description = "ID do ingrediente", required = true) @PathVariable UUID id) {
        service.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
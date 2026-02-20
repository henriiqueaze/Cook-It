package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import com.p5Project.cookIt.services.PantryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pantryItem")
public class PantryItemController {

    @Autowired
    private PantryItemService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO findPantryItemById(@PathVariable UUID id) {
        return service.findPantryItemById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<PantryItemDTO> findAllPantryItems() {
        return service.findAllPantryItems();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO createPantryItem(@RequestBody PantryItemDTO comment) {
        return service.createPantryItem(comment);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO updatePantryItem(@RequestBody PantryItemDTO comment) {
        return service.updatePantryItem(comment);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public PantryItemDTO updatePantryItemField(@PathVariable UUID id, @RequestBody PantryItemDTO comment) {
        return service.updatePantryItemField(id, comment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePantryItem(@PathVariable UUID id) {
        service.deletePantryItem(id);
        return ResponseEntity.noContent().build();
    }
}

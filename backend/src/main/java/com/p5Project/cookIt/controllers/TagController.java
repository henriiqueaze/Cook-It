package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.TagDTO;
import com.p5Project.cookIt.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public TagDTO findTagById(@PathVariable UUID id) {
        return service.findTagById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<TagDTO> findAllTags() {
        return service.findAllTags();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public TagDTO createTag(@RequestBody TagDTO tag) {
        return service.createTag(tag);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public TagDTO updateTag(@RequestBody TagDTO tag) {
        return service.updateTag(tag);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public TagDTO updateTagField(@PathVariable UUID id, @RequestBody TagDTO tag) {
        return service.updateTagField(id, tag);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeTag(@PathVariable UUID id) {
        service.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}

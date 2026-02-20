package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO findCommentById(@PathVariable UUID id) {
        return service.findCommentById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<CommentDTO> findAllComments() {
        return service.findAllComments();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO createComment(@RequestBody CommentDTO comment) {
        return service.createComment(comment);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO updateComment(@RequestBody CommentDTO comment) {
        return service.updateComment(comment);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public CommentDTO updateCommentField(@PathVariable UUID id, @RequestBody CommentDTO comment) {
        return service.updateCommentField(id, comment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id) {
        service.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

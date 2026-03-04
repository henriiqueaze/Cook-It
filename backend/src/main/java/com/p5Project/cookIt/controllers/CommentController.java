package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.CommentControllerDocs;
import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.services.CommentService;
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
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDocs {

    @Autowired
    private CommentService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public CommentDTO findCommentById(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id) {
        return service.findCommentById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<CommentDTO>>> findAllComments(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "content"));
        return ResponseEntity.ok(service.findAllComments(pageable));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public CommentDTO createComment(@RequestBody(description = "Objeto contendo os dados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.createComment(comment);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public CommentDTO updateComment(@RequestBody(description = "Objeto contendo os dados atualizados do comentário", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.updateComment(comment);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public CommentDTO updateCommentField(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id,
                                         @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = CommentDTO.class))) CommentDTO comment) {
        return service.updateCommentField(id, comment);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteComment(@Parameter(description = "ID do comentário", required = true) @PathVariable UUID id) {
        service.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
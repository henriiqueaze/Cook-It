package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.RatingControllerDocs;
import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.services.RatingService;
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
@RequestMapping("/rating")
@Tag(name = "Rating", description = "Endpoints para gerenciamento de avaliações das receitas")
public class RatingController implements RatingControllerDocs {

    @Autowired
    private RatingService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RatingDTO findRatingById(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id) {
        return service.findRatingById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<RatingDTO>>> findAllRatings(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAllRatings(pageable));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RatingDTO createRating(@RequestBody(description = "Objeto contendo os dados da avaliação", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.createRating(rating);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RatingDTO updateRating(@RequestBody(description = "Objeto contendo os dados atualizados da avaliação", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.updateRating(rating);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public RatingDTO updateRatingField(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id,
                                       @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = RatingDTO.class))) @org.springframework.web.bind.annotation.RequestBody RatingDTO rating) {
        return service.updateRatingField(id, rating);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteRating(@Parameter(description = "ID da avaliação", required = true) @PathVariable UUID id) {
        service.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
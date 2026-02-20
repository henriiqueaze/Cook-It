package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO findRatingById(@PathVariable UUID id) {
        return service.findRatingById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<RatingDTO> findAllRating() {
        return service.findAllRatings();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO createRating(@RequestBody RatingDTO rating) {
        return service.createRating(rating);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO updateRating(@RequestBody RatingDTO rating) {
        return service.updateRating(rating);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public RatingDTO updateRatingField(@PathVariable UUID id, @RequestBody RatingDTO rating) {
        return service.updateRatingField(id, rating);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable UUID id) {
        service.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}

package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.PantryItemController;
import com.p5Project.cookIt.controllers.RatingController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.models.entities.Rating;
import com.p5Project.cookIt.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RatingService {

    @Autowired
    private RatingRepository repository;

    public RatingDTO findRatingById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public List<RatingDTO> findAllRatings() {
        return Mapper.parseItemsList(repository.findAll(), RatingDTO.class);
    }

    public RatingDTO createRating(RatingDTO rating) {
        var entity = Mapper.parseItem(rating, Rating.class);
        repository.save(entity);
        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public RatingDTO updateRating(RatingDTO rating) {
        var entity = repository.findById(rating.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(rating, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public RatingDTO updateRatingField(UUID id, RatingDTO rating) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(rating, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public void deleteRating(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }

    private void addHATEOASLinks(RatingDTO rating) {
        rating.add(linkTo(methodOn(RatingController.class).findRatingById(rating.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        rating.add(linkTo(methodOn(RatingController.class).createRating(rating)).withRel("create").withType("POST"));
        rating.add(linkTo(methodOn(RatingController.class).updateRating(rating)).withRel("update").withType("PUT"));
        rating.add(linkTo(methodOn(RatingController.class).updateRatingField(rating.getId(), rating)).withRel("patch").withType("PATCH"));
        rating.add(linkTo(methodOn(RatingController.class).deleteRating(rating.getId())).withRel("delete").withType("DELETE"));
    }
}

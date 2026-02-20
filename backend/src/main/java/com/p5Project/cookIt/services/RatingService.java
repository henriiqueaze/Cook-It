package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.models.entities.Rating;
import com.p5Project.cookIt.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingService {

    @Autowired
    private RatingRepository repository;

    public RatingDTO findById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public List<RatingDTO> findAll() {
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

    public RatingDTO updateFieldRating(UUID id, RatingDTO rating) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(rating, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RatingDTO.class);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

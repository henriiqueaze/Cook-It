package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.IngredientDTO;
import com.p5Project.cookIt.models.entities.Ingredient;
import com.p5Project.cookIt.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository repository;

    public IngredientDTO findIngredientById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, IngredientDTO.class);
    }

    public List<IngredientDTO> findAllIngredients() {
        return Mapper.parseItemsList(repository.findAll(), IngredientDTO.class);
    }

    public IngredientDTO createIngredient(IngredientDTO ingredient) {
        var entity = Mapper.parseItem(ingredient, Ingredient.class);
        repository.save(entity);
        return Mapper.parseItem(entity, IngredientDTO.class);
    }

    public IngredientDTO updateIngredient(IngredientDTO ingredient) {
        var entity = repository.findById(ingredient.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(ingredient, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, IngredientDTO.class);
    }

    public IngredientDTO updateIngredientField(UUID id, IngredientDTO ingredient) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(ingredient, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, IngredientDTO.class);
    }

    public void deleteIngredient(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

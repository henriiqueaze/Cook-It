package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.models.entities.RecipeIngredient;
import com.p5Project.cookIt.repositories.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository repository;

    public RecipeIngredientDTO findRecipeIngredientById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RecipeIngredientDTO.class);
    }

    public List<RecipeIngredientDTO> findAllRecipeIngredients() {
        return Mapper.parseItemsList(repository.findAll(), RecipeIngredientDTO.class);
    }

    public RecipeIngredientDTO createRecipeIngredient(RecipeIngredientDTO dto) {
        var entity = Mapper.parseItem(dto, RecipeIngredient.class);
        repository.save(entity);
        return Mapper.parseItem(entity, RecipeIngredientDTO.class);
    }

    public RecipeIngredientDTO updateRecipeIngredient(RecipeIngredientDTO dto) {
        var entity = repository.findById(dto.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(dto, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeIngredientDTO.class);
    }

    public RecipeIngredientDTO updateRecipeIngredientField(UUID id, RecipeIngredientDTO dto) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(dto, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeIngredientDTO.class);
    }

    public void deleteRecipeIngredient(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

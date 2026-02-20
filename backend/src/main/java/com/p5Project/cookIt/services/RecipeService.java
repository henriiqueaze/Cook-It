package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;

    public RecipeDTO findRecipeById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RecipeDTO.class);
    }

    public List<RecipeDTO> findAllRecipes() {
        return Mapper.parseItemsList(repository.findAll(), RecipeDTO.class);
    }

    public RecipeDTO createRecipe(RecipeDTO recipe) {
        var entity = Mapper.parseItem(recipe, Recipe.class);
        repository.save(entity);
        return Mapper.parseItem(entity, RecipeDTO.class);
    }

    public RecipeDTO updateRecipe(RecipeDTO recipe) {
        var entity = repository.findById(recipe.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipe, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeDTO.class);
    }

    public RecipeDTO updateRecipeField(UUID id, RecipeDTO recipe) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipe, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeDTO.class);
    }

    public void deleteRecipe(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

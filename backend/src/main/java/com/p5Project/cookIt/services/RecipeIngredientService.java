package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RatingController;
import com.p5Project.cookIt.controllers.RecipeController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RatingDTO;
import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.models.entities.RecipeIngredient;
import com.p5Project.cookIt.repositories.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    private void addHATEOASLinks(RecipeIngredientDTO recipeIngredient) {
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientService.class).findRecipeIngredientById(recipeIngredient.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientService.class).createRecipeIngredient(recipeIngredient)).withRel("create").withType("POST"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientService.class).updateRecipeIngredient(recipeIngredient)).withRel("update").withType("PUT"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientService.class).updateRecipeIngredientField(recipeIngredient.getId(), recipeIngredient)).withRel("patch").withType("PATCH"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientService.class).deleteRecipeIngredient(recipeIngredient.getId())).withRel("delete").withType("DELETE"));
    }
}

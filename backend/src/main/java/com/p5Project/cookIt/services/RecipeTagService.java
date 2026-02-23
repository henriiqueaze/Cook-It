package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RecipeController;
import com.p5Project.cookIt.controllers.RecipeTagController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.models.entities.RecipeTag;
import com.p5Project.cookIt.repositories.RecipeTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RecipeTagService {

    @Autowired
    private RecipeTagRepository repository;

    public RecipeTagDTO findRecipeTagById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RecipeTagDTO.class);
    }

    public List<RecipeTagDTO> findAllRecipeTags() {
        var entities = repository.findAll();
        return Mapper.parseItemsList(entities, RecipeTagDTO.class);
    }

    public RecipeTagDTO createRecipeTag(RecipeTagDTO recipeTag) {
        var entity = Mapper.parseItem(recipeTag, RecipeTag.class);
        repository.save(entity);
        return Mapper.parseItem(entity, RecipeTagDTO.class);
    }

    public RecipeTagDTO updateRecipeTag(RecipeTagDTO recipeTag) {
        var entity = repository.findById(recipeTag.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipeTag, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeTagDTO.class);
    }

    public RecipeTagDTO updateRecipeTagField(UUID id, RecipeTagDTO recipeTag) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipeTag, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, RecipeTagDTO.class);
    }

    public void deleteRecipeTag(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }

    private void addHATEOASLinks(RecipeTagDTO recipeTag) {
        recipeTag.add(linkTo(methodOn(RecipeTagController.class).findRecipeTagById(recipeTag.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        recipeTag.add(linkTo(methodOn(RecipeTagController.class).createRecipeTag(recipeTag)).withRel("create").withType("POST"));
        recipeTag.add(linkTo(methodOn(RecipeTagController.class).updateRecipeTag(recipeTag)).withRel("update").withType("PUT"));
        recipeTag.add(linkTo(methodOn(RecipeTagController.class).updateRecipeTagField(recipeTag.getId(), recipeTag)).withRel("patch").withType("PATCH"));
        recipeTag.add(linkTo(methodOn(RecipeTagController.class).deleteRecipeTag(recipeTag.getId())).withRel("delete").withType("DELETE"));
    }
}

package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RecipeController;
import com.p5Project.cookIt.controllers.RecipeIngredientController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private PagedResourcesAssembler<RecipeDTO> assembler;

    public RecipeDTO findRecipeById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, RecipeDTO.class);
    }

    public PagedModel<EntityModel<RecipeDTO>> findAllRecipes(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(recipe -> {
            var dto = Mapper.parseItem(recipe, RecipeDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).findAllRating(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
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

        var dto = Mapper.parseItem(entity, RecipeDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public RecipeDTO updateRecipeField(UUID id, RecipeDTO recipe) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipe, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, RecipeDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteRecipe(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, RecipeDTO.class);
        addHATEOASLinks(dto);
        repository.delete(entity);
    }

    private void addHATEOASLinks(RecipeDTO recipe) {
        recipe.add(linkTo(methodOn(RecipeController.class).findRatingById(recipe.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        recipe.add(linkTo(methodOn(RecipeController.class).createRating(recipe)).withRel("create").withType("POST"));
        recipe.add(linkTo(methodOn(RecipeController.class).updateRating(recipe)).withRel("update").withType("PUT"));
        recipe.add(linkTo(methodOn(RecipeController.class).updateRatingField(recipe.getId(), recipe)).withRel("patch").withType("PATCH"));
        recipe.add(linkTo(methodOn(RecipeController.class).deleteRating(recipe.getId())).withRel("delete").withType("DELETE"));
    }
}

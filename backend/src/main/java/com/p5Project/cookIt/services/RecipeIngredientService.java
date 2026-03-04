package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RecipeIngredientController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.models.entities.RecipeIngredient;
import com.p5Project.cookIt.repositories.RecipeIngredientRepository;
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
public class RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository repository;

    @Autowired
    private PagedResourcesAssembler<RecipeIngredientDTO> assembler;

    public RecipeIngredientDTO findRecipeIngredientById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        var dto = Mapper.parseItem(entity, RecipeIngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<RecipeIngredientDTO>> findAllRecipeIngredients(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(recipeIngredient -> {
            var dto = Mapper.parseItem(recipeIngredient, RecipeIngredientDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeIngredientController.class).findAllRecipeIngredients(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public RecipeIngredientDTO createRecipeIngredient(RecipeIngredientDTO reciptIngDTO) {
        var entity = Mapper.parseItem(reciptIngDTO, RecipeIngredient.class);
        repository.save(entity);
        var dto = Mapper.parseItem(entity, RecipeIngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public RecipeIngredientDTO updateRecipeIngredient(RecipeIngredientDTO recipeIngDTO) {
        var entity = repository.findById(recipeIngDTO.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipeIngDTO, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, RecipeIngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public RecipeIngredientDTO updateRecipeIngredientField(UUID id, RecipeIngredientDTO recipeIngDTO) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(recipeIngDTO, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, RecipeIngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteRecipeIngredient(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, RecipeIngredientDTO.class);
        addHATEOASLinks(dto);

        repository.delete(entity);
    }

    private void addHATEOASLinks(RecipeIngredientDTO recipeIngredient) {
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).findRecipeIngredientById(recipeIngredient.getId())).withSelfRel().withType("GET"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).findAllRecipeIngredients(0, 12, "asc")).withRel("findAll").withType("GET"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).createRecipeIngredient(recipeIngredient)).withRel("create").withType("POST"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).updateRecipeIngredient(recipeIngredient)).withRel("update").withType("PUT"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).updateRecipeIngredientField(recipeIngredient.getId(), recipeIngredient)).withRel("patch").withType("PATCH"));
        recipeIngredient.add(linkTo(methodOn(RecipeIngredientController.class).deleteRecipeIngredient(recipeIngredient.getId())).withRel("delete").withType("DELETE"));
    }
}

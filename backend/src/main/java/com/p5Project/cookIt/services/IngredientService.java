package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.ImageController;
import com.p5Project.cookIt.controllers.IngredientController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.ImageDTO;
import com.p5Project.cookIt.models.dtos.IngredientDTO;
import com.p5Project.cookIt.models.entities.Ingredient;
import com.p5Project.cookIt.repositories.IngredientRepository;
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
public class IngredientService {

    @Autowired
    private IngredientRepository repository;

    @Autowired
    private PagedResourcesAssembler<IngredientDTO> assembler;

    public IngredientDTO findIngredientById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        var dto = Mapper.parseItem(entity, IngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<IngredientDTO>> findAllIngredients(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(ingredient -> {
            var dto = Mapper.parseItem(ingredient, IngredientDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IngredientController.class).findAllIngredients(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public IngredientDTO createIngredient(IngredientDTO ingredient) {
        var entity = Mapper.parseItem(ingredient, Ingredient.class);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, IngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public IngredientDTO updateIngredient(IngredientDTO ingredient) {
        var entity = repository.findById(ingredient.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(ingredient, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, IngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public IngredientDTO updateIngredientField(UUID id, IngredientDTO ingredient) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(ingredient, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, IngredientDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteIngredient(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, IngredientDTO.class);
        addHATEOASLinks(dto);

        repository.delete(entity);
    }

    private void addHATEOASLinks(IngredientDTO ingredient) {
        ingredient.add(linkTo(methodOn(IngredientController.class).findIngredientById(ingredient.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        ingredient.add(linkTo(methodOn(IngredientController.class).createIngredient(ingredient)).withRel("create").withType("POST"));
        ingredient.add(linkTo(methodOn(IngredientController.class).updateIngredient(ingredient)).withRel("update").withType("PUT"));
        ingredient.add(linkTo(methodOn(IngredientController.class).updateIngredientField(ingredient.getId(), ingredient)).withRel("patch").withType("PATCH"));
        ingredient.add(linkTo(methodOn(IngredientController.class).deleteComment(ingredient.getId())).withRel("delete").withType("DELETE"));
    }
}

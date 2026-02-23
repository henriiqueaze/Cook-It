package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.IngredientController;
import com.p5Project.cookIt.controllers.PantryItemController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.models.dtos.IngredientDTO;
import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import com.p5Project.cookIt.models.entities.PantryItem;
import com.p5Project.cookIt.repositories.PantryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PantryItemService {

    @Autowired
    private PantryItemRepository repository;

    public PantryItemDTO findPantryItemById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        var dto = Mapper.parseItem(entity, PantryItemDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public List<PantryItemDTO> findAllPantryItems() {
        return Mapper.parseItemsList(repository.findAll(), PantryItemDTO.class);
    }

    public PantryItemDTO createPantryItem(PantryItemDTO pantryItem) {
        var entity = Mapper.parseItem(pantryItem, PantryItem.class);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, PantryItemDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PantryItemDTO updatePantryItem(PantryItemDTO pantryItem) {
        var entity = repository.findById(pantryItem.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(pantryItem, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, PantryItemDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PantryItemDTO updatePantryItemField(UUID id, PantryItemDTO pantryItem) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(pantryItem, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, PantryItemDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deletePantryItem(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, PantryItemDTO.class);
        addHATEOASLinks(dto);

        repository.delete(entity);
    }

    private void addHATEOASLinks(PantryItemDTO pantryItem) {
        pantryItem.add(linkTo(methodOn(PantryItemController.class).findPantryItemById(pantryItem.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        pantryItem.add(linkTo(methodOn(PantryItemController.class).createPantryItem(pantryItem)).withRel("create").withType("POST"));
        pantryItem.add(linkTo(methodOn(PantryItemController.class).updatePantryItem(pantryItem)).withRel("update").withType("PUT"));
        pantryItem.add(linkTo(methodOn(PantryItemController.class).updatePantryItemField(pantryItem.getId(), pantryItem)).withRel("patch").withType("PATCH"));
        pantryItem.add(linkTo(methodOn(PantryItemController.class).deletePantryItem(pantryItem.getId())).withRel("delete").withType("DELETE"));
    }
}

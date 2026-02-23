package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RecipeTagController;
import com.p5Project.cookIt.controllers.TagController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.models.dtos.TagDTO;
import com.p5Project.cookIt.models.entities.Tag;
import com.p5Project.cookIt.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    public TagDTO findTagById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, TagDTO.class);
    }

    public List<TagDTO> findAllTags() {
        var entities = repository.findAll();
        return Mapper.parseItemsList(entities, TagDTO.class);
    }

    public TagDTO createTag(TagDTO tag) {
        var entity = Mapper.parseItem(tag, Tag.class);
        repository.save(entity);
        return Mapper.parseItem(entity, TagDTO.class);
    }

    public TagDTO updateTag(TagDTO tag) {
        var entity = repository.findById(tag.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(tag, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, TagDTO.class);
    }

    public TagDTO updateTagField(UUID id, TagDTO tag) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(tag, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, TagDTO.class);
    }

    public void deleteTag(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }

    private void addHATEOASLinks(TagDTO tag) {
        tag.add(linkTo(methodOn(TagController.class).findTagById(tag.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        tag.add(linkTo(methodOn(TagController.class).createTag(tag)).withRel("create").withType("POST"));
        tag.add(linkTo(methodOn(TagController.class).updateTag(tag)).withRel("update").withType("PUT"));
        tag.add(linkTo(methodOn(TagController.class).updateTagField(tag.getId(), tag)).withRel("patch").withType("PATCH"));
        tag.add(linkTo(methodOn(TagController.class).deleteRecipeTag(tag.getId())).withRel("delete").withType("DELETE"));
    }
}

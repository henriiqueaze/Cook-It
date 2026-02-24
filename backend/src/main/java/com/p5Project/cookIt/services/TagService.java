package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.RecipeController;
import com.p5Project.cookIt.controllers.RecipeTagController;
import com.p5Project.cookIt.controllers.TagController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.RecipeDTO;
import com.p5Project.cookIt.models.dtos.RecipeTagDTO;
import com.p5Project.cookIt.models.dtos.TagDTO;
import com.p5Project.cookIt.models.entities.Tag;
import com.p5Project.cookIt.repositories.TagRepository;
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
public class TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private PagedResourcesAssembler<TagDTO> assembler;

    public TagDTO findTagById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        var dto = Mapper.parseItem(entity, TagDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<TagDTO>> findAllTags(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(tag -> {
            var dto = Mapper.parseItem(tag, TagDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).findAllTags(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public TagDTO createTag(TagDTO tag) {
        var entity = Mapper.parseItem(tag, Tag.class);
        repository.save(entity);
        var dto = Mapper.parseItem(entity, TagDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public TagDTO updateTag(TagDTO tag) {
        var entity = repository.findById(tag.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(tag, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, TagDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public TagDTO updateTagField(UUID id, TagDTO tag) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(tag, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, TagDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteTag(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, TagDTO.class);
        addHATEOASLinks(dto);
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

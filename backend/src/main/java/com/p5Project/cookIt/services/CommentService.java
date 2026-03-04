package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.CommentController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.models.entities.Comment;
import com.p5Project.cookIt.repositories.CommentRepository;
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
public class CommentService {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private PagedResourcesAssembler<CommentDTO> assembler;

    public CommentDTO findCommentById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        var dto = Mapper.parseItem(entity, CommentDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<CommentDTO>> findAllComments(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(comment -> {
            var dto = Mapper.parseItem(comment, CommentDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class).findAllComments(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public CommentDTO createComment(CommentDTO comment) {
        var entity = Mapper.parseItem(comment, Comment.class);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public CommentDTO updateComment(CommentDTO comment) {
        var entity = repository.findById(comment.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(comment, entity);

        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public CommentDTO updateCommentField(UUID id, CommentDTO comment) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        Mapper.mapNonNullFields(comment, entity);

        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteComment(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, CommentDTO.class);
        addHATEOASLinks(dto);

        repository.delete(entity);
    }

    private void addHATEOASLinks(CommentDTO comment) {
        comment.add(linkTo(methodOn(CommentController.class).findCommentById(comment.getId())).withSelfRel().withType("GET"));
        comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        comment.add(linkTo(methodOn(CommentController.class).createComment(comment)).withRel("create").withType("POST"));
        comment.add(linkTo(methodOn(CommentController.class).updateComment(comment)).withRel("update").withType("PUT"));
        comment.add(linkTo(methodOn(CommentController.class).updateCommentField(comment.getId(), comment)).withRel("patch").withType("PATCH"));
        comment.add(linkTo(methodOn(CommentController.class).deleteComment(comment.getId())).withRel("delete").withType("DELETE"));
    }
}

package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.UserController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.UserDTO;
import com.p5Project.cookIt.models.entities.User;
import com.p5Project.cookIt.repositories.UserRepository;
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
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PagedResourcesAssembler<UserDTO> assembler;

    public UserDTO findUserById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        var dto = Mapper.parseItem(entity, UserDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<UserDTO>> findAllUsers(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(user -> {
            var dto = Mapper.parseItem(user, UserDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).findAllUsers(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public UserDTO createUser(UserDTO user) {
        var entity = Mapper.parseItem(user, User.class);
        repository.save(entity);
        var dto = Mapper.parseItem(entity, UserDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public UserDTO updateUser(UserDTO user) {
        var entity = repository.findById(user.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(user, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, UserDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public UserDTO updateUserField(UUID id, UserDTO user) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(user, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, UserDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteUser(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, UserDTO.class);
        addHATEOASLinks(dto);
        repository.delete(entity);
    }

    private void addHATEOASLinks(UserDTO user) {
        user.add(linkTo(methodOn(UserController.class).findUserById(user.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        user.add(linkTo(methodOn(UserController.class).createUser(user)).withRel("create").withType("POST"));
        user.add(linkTo(methodOn(UserController.class).updateUser(user)).withRel("update").withType("PUT"));
        user.add(linkTo(methodOn(UserController.class).updateUserField(user.getId(), user)).withRel("patch").withType("PATCH"));
        user.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete").withType("DELETE"));
    }
}

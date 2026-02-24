package com.p5Project.cookIt.services;

import com.p5Project.cookIt.controllers.CommentController;
import com.p5Project.cookIt.controllers.ImageController;
import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.models.dtos.ImageDTO;
import com.p5Project.cookIt.models.entities.Image;
import com.p5Project.cookIt.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    @Autowired
    private PagedResourcesAssembler<ImageDTO> assembler;

    public ImageDTO findImageById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        var dto = Mapper.parseItem(entity, ImageDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<ImageDTO>> findAllImages(Pageable pageable) {
        var entities = repository.findAll(pageable);

        var commentsWithLinks = entities.map(image -> {
            var dto = Mapper.parseItem(image, ImageDTO.class);
            addHATEOASLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ImageController.class).findAllImages(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();
        return assembler.toModel(commentsWithLinks, findAllLink);
    }

    public ImageDTO createImage(ImageDTO image) {
        var entity = Mapper.parseItem(image, Image.class);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, ImageDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public ImageDTO updateImage(ImageDTO image) {
        var entity = repository.findById(image.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(image, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, ImageDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public ImageDTO updateImageField(UUID id, ImageDTO image) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(image, entity);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, ImageDTO.class);
        addHATEOASLinks(dto);

        return dto;
    }

    public void deleteImage(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, ImageDTO.class);
        addHATEOASLinks(dto);

        repository.delete(entity);
    }

    private void addHATEOASLinks(ImageDTO image) {
        image.add(linkTo(methodOn(ImageController.class).findImageById(image.getId())).withSelfRel().withType("GET"));
        //comment.add(linkTo(methodOn(CommentController.class).findAllComments(0, 12, "asc")).withRel("findAll").withType("GET"));
        image.add(linkTo(methodOn(ImageController.class).createImage(image)).withRel("create").withType("POST"));
        image.add(linkTo(methodOn(ImageController.class).updateImage(image)).withRel("update").withType("PUT"));
        image.add(linkTo(methodOn(ImageController.class).updateImageField(image.getId(), image)).withRel("patch").withType("PATCH"));
        image.add(linkTo(methodOn(ImageController.class).deleteImage(image.getId())).withRel("delete").withType("DELETE"));
    }
}

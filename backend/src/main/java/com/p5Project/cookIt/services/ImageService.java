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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public ImageDTO findById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, ImageDTO.class);
    }

    public List<ImageDTO> findAll() {
        var entities = repository.findAll();
        return Mapper.parseItemsList(entities, ImageDTO.class);
    }

    public ImageDTO createImage(ImageDTO image) {
        var entity = Mapper.parseItem(image, Image.class);
        repository.save(entity);
        return Mapper.parseItem(entity, ImageDTO.class);
    }

    public ImageDTO updateImage(ImageDTO image) {
        var entity = repository.findById(image.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(image, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, ImageDTO.class);
    }

    public ImageDTO updateImageField(UUID id, ImageDTO image) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(image, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, ImageDTO.class);
    }

    public void deleteImage(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
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

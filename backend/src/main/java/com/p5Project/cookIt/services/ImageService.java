package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.ImageDTO;
import com.p5Project.cookIt.models.entities.Image;
import com.p5Project.cookIt.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public ImageDTO updateFieldImage(UUID id, ImageDTO image) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(image, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, ImageDTO.class);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

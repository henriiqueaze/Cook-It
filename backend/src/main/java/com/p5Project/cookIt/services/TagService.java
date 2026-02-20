package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.TagDTO;
import com.p5Project.cookIt.models.entities.Tag;
import com.p5Project.cookIt.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    public TagDTO findById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, TagDTO.class);
    }

    public List<TagDTO> findAll() {
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

    public TagDTO updateFieldTag(UUID id, TagDTO tag) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(tag, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, TagDTO.class);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}

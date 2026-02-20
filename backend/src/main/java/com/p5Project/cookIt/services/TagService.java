package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.tag.CreateTagDTO;
import com.p5Project.cookIt.models.dtos.tag.TagDTO;
import com.p5Project.cookIt.models.entities.Tag;
import com.p5Project.cookIt.repositories.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository repo;
    private final ModelMapper mapper;

    public TagService(TagRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public TagDTO create(CreateTagDTO dto) {
        Tag t = mapper.map(dto, Tag.class);
        Tag saved = repo.save(t);
        return mapper.map(saved, TagDTO.class);
    }

    @Transactional(readOnly = true)
    public TagDTO findById(UUID id) {
        Tag t = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return mapper.map(t, TagDTO.class);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, TagDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public TagDTO update(UUID id, CreateTagDTO dto) {
        Tag entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        mapper.map(dto, entity);
        Tag saved = repo.save(entity);
        return mapper.map(saved, TagDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Tag not found");
        repo.deleteById(id);
    }
}

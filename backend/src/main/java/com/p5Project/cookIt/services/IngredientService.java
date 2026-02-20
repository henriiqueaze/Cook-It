package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.ingredient.CreateIngredientDTO;
import com.p5Project.cookIt.models.dtos.ingredient.IngredientDTO;
import com.p5Project.cookIt.models.entities.Ingredient;
import com.p5Project.cookIt.repositories.IngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository repo;
    private final ModelMapper mapper;

    public IngredientService(IngredientRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public IngredientDTO create(CreateIngredientDTO dto) {
        Ingredient ent = mapper.map(dto, Ingredient.class);
        Ingredient saved = repo.save(ent);
        return mapper.map(saved, IngredientDTO.class);
    }

    @Transactional(readOnly = true)
    public IngredientDTO findById(UUID id) {
        Ingredient ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        return mapper.map(ent, IngredientDTO.class);
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> findAll() {
        return repo.findAll().stream().map(i -> mapper.map(i, IngredientDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public IngredientDTO update(UUID id, CreateIngredientDTO dto) {
        Ingredient ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        mapper.map(dto, ent);
        Ingredient saved = repo.save(ent);
        return mapper.map(saved, IngredientDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Ingredient not found");
        repo.deleteById(id);
    }
}
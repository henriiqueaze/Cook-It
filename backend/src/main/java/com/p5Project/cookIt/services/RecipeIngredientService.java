package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import com.p5Project.cookIt.models.dtos.recipeIngredient.RecipeIngredientDTO;
import com.p5Project.cookIt.models.entities.RecipeIngredient;
import com.p5Project.cookIt.repositories.IngredientRepository;
import com.p5Project.cookIt.repositories.RecipeIngredientRepository;
import com.p5Project.cookIt.repositories.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository repo;
    private final RecipeRepository recipeRepo;
    private final IngredientRepository ingredientRepo;
    private final ModelMapper mapper;

    public RecipeIngredientService(RecipeIngredientRepository repo, RecipeRepository recipeRepo, IngredientRepository ingredientRepo, ModelMapper mapper) {
        this.repo = repo;
        this.recipeRepo = recipeRepo;
        this.ingredientRepo = ingredientRepo;
        this.mapper = mapper;
    }

    @Transactional
    public RecipeIngredientDTO create(UUID recipeId, RecipeIngredientCreateDTO dto) {
        RecipeIngredient ri = mapper.map(dto, RecipeIngredient.class);
        if (recipeId != null) ri.setRecipe(recipeRepo.getReferenceById(recipeId));
        if (dto.getIngredientId() != null) ri.setIngredient(ingredientRepo.getReferenceById(dto.getIngredientId()));
        RecipeIngredient saved = repo.save(ri);
        return mapper.map(saved, RecipeIngredientDTO.class);
    }

    @Transactional(readOnly = true)
    public RecipeIngredientDTO findById(UUID id) {
        RecipeIngredient e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("RecipeIngredient not found"));
        return mapper.map(e, RecipeIngredientDTO.class);
    }

    @Transactional(readOnly = true)
    public List<RecipeIngredientDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, RecipeIngredientDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public RecipeIngredientDTO update(UUID id, RecipeIngredientCreateDTO dto) {
        RecipeIngredient ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("RecipeIngredient not found"));
        mapper.map(dto, ent);
        if (dto.getIngredientId() != null) ent.setIngredient(ingredientRepo.getReferenceById(dto.getIngredientId()));
        RecipeIngredient saved = repo.save(ent);
        return mapper.map(saved, RecipeIngredientDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("RecipeIngredient not found");
        repo.deleteById(id);
    }
}

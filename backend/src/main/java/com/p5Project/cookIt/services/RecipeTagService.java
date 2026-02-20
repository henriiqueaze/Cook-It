package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.recipeTag.RecipeTagDTO;
import com.p5Project.cookIt.models.entities.RecipeTag;
import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.Tag;
import com.p5Project.cookIt.repositories.RecipeRepository;
import com.p5Project.cookIt.repositories.RecipeTagRepository;
import com.p5Project.cookIt.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeTagService {

    private final RecipeTagRepository repo;
    private final RecipeRepository recipeRepo;
    private final TagRepository tagRepo;

    public RecipeTagService(
            RecipeTagRepository repo,
            RecipeRepository recipeRepo,
            TagRepository tagRepo
    ) {
        this.repo = repo;
        this.recipeRepo = recipeRepo;
        this.tagRepo = tagRepo;
    }

    @Transactional
    public RecipeTagDTO create(UUID recipeId, UUID tagId) {
        RecipeTag rt = new RecipeTag();

        Recipe recipe = recipeRepo.getReferenceById(recipeId);
        Tag tag = tagRepo.getReferenceById(tagId);

        rt.setRecipe(recipe);
        rt.setTag(tag);

        RecipeTag saved = repo.save(rt);

        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<RecipeTagDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecipeTagDTO findById(UUID id) {
        RecipeTag entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecipeTag not found"));

        return toDTO(entity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("RecipeTag not found");
        }
        repo.deleteById(id);
    }

    private RecipeTagDTO toDTO(RecipeTag entity) {
        return RecipeTagDTO.builder()
                .id(entity.getId())
                .recipeId(entity.getRecipe().getId())
                .tagId(entity.getTag().getId())
                .build();
    }
}
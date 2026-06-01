package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.IngredientDTO;
import com.p5Project.cookIt.entities.Ingredient;
import com.p5Project.cookIt.mappers.IngredientMapper;
import com.p5Project.cookIt.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final ModerationService moderationService;

    @Transactional(readOnly = true)
    public List<IngredientDTO> getAll() {
        return ingredientMapper.toDTOList(ingredientRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> search(String query) {
        String normalized = normalizeOrNull(query);
        if (normalized == null) {
            return List.of();
        }

        return ingredientMapper.toDTOList(
                ingredientRepository.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(normalized)
        );
    }

    @Transactional
    public Ingredient findOrCreate(String name) {
        String normalized = normalize(name);
        moderationService.ensureIngredientAllowed(normalized);

        return ingredientRepository.findByNameIgnoreCase(normalized).orElseGet(() -> createAndSave(normalized));
    }

    @Transactional
    public IngredientDTO create(String name) {
        return ingredientMapper.toDTO(findOrCreate(name));
    }

    private Ingredient createAndSave(String normalizedName) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(normalizedName);
        return ingredientRepository.saveAndFlush(ingredient);
    }

    private String normalizeOrNull(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }

    private String normalize(String value) {
        String normalized = normalizeOrNull(value);
        if (normalized == null) {
            throw new IllegalArgumentException("Ingredient name cannot be null or blank");
        }
        return normalized;
    }
}

package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.recipe.CreateRecipeDTO;
import com.p5Project.cookIt.models.dtos.recipe.RecipeDTO;
import com.p5Project.cookIt.models.dtos.recipe.UpdateRecipeDTO;
import com.p5Project.cookIt.models.entities.*;
import com.p5Project.cookIt.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepo;
    private final UserRepository userRepo;
    private final IngredientRepository ingredientRepo;
    private final RecipeIngredientRepository recipeIngredientRepo;
    private final RecipeTagRepository recipeTagRepo;
    private final TagRepository tagRepo;
    private final ModelMapper mapper;

    public RecipeService(
            RecipeRepository recipeRepo,
            UserRepository userRepo,
            IngredientRepository ingredientRepo,
            RecipeIngredientRepository recipeIngredientRepo,
            RecipeTagRepository recipeTagRepo,
            TagRepository tagRepo,
            ModelMapper mapper
    ) {
        this.recipeRepo = recipeRepo;
        this.userRepo = userRepo;
        this.ingredientRepo = ingredientRepo;
        this.recipeIngredientRepo = recipeIngredientRepo;
        this.recipeTagRepo = recipeTagRepo;
        this.tagRepo = tagRepo;
        this.mapper = mapper;
    }

    @Transactional
    public RecipeDTO create(CreateRecipeDTO dto) {

        Recipe recipe = mapper.map(dto, Recipe.class);

        if (dto.getAuthorId() != null) {
            recipe.setAuthor(userRepo.getReferenceById(dto.getAuthorId()));
        }

        Recipe saved = recipeRepo.save(recipe);

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {

            List<RecipeIngredient> ris = dto.getIngredients().stream().map(iDto -> {

                RecipeIngredient ri = mapper.map(iDto, RecipeIngredient.class);
                ri.setRecipe(saved);

                if (iDto.getIngredientId() != null) {
                    ri.setIngredient(
                            ingredientRepo.getReferenceById(iDto.getIngredientId())
                    );
                }

                return ri;
            }).collect(Collectors.toList());

            recipeIngredientRepo.saveAll(ris);
            saved.setRecipeIngredients(ris);
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {

            List<RecipeTag> rts = dto.getTagIds().stream().map(tagId -> {

                RecipeTag rt = new RecipeTag();
                rt.setRecipe(saved);
                rt.setTag(tagRepo.getReferenceById(tagId));

                return rt;
            }).collect(Collectors.toList());

            recipeTagRepo.saveAll(rts);
            saved.setRecipeTags(rts);
        }

        return mapper.map(saved, RecipeDTO.class);
    }

    @Transactional(readOnly = true)
    public RecipeDTO findById(UUID id) {

        Recipe recipe = recipeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        return mapper.map(recipe, RecipeDTO.class);
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> findAll() {

        return recipeRepo.findAll()
                .stream()
                .map(recipe -> mapper.map(recipe, RecipeDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeDTO update(UUID id, UpdateRecipeDTO dto) {

        Recipe recipe = recipeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        mapper.map(dto, recipe);

        Recipe saved = recipeRepo.save(recipe);

        if (dto.getIngredients() != null) {

            List<RecipeIngredient> oldIngredients =
                    recipeIngredientRepo.findAll()
                            .stream()
                            .filter(ri -> ri.getRecipe() != null &&
                                    ri.getRecipe().getId().equals(saved.getId()))
                            .collect(Collectors.toList());

            recipeIngredientRepo.deleteAll(oldIngredients);

            List<RecipeIngredient> newIngredients =
                    dto.getIngredients().stream().map(iDto -> {

                        RecipeIngredient ri = mapper.map(iDto, RecipeIngredient.class);
                        ri.setRecipe(saved);

                        if (iDto.getIngredientId() != null) {
                            ri.setIngredient(
                                    ingredientRepo.getReferenceById(iDto.getIngredientId())
                            );
                        }

                        return ri;

                    }).collect(Collectors.toList());

            recipeIngredientRepo.saveAll(newIngredients);
            saved.setRecipeIngredients(newIngredients);
        }

        if (dto.getTagIds() != null) {

            List<RecipeTag> oldTags =
                    recipeTagRepo.findAll()
                            .stream()
                            .filter(rt -> rt.getRecipe() != null &&
                                    rt.getRecipe().getId().equals(saved.getId()))
                            .collect(Collectors.toList());

            recipeTagRepo.deleteAll(oldTags);

            List<RecipeTag> newTags =
                    dto.getTagIds().stream().map(tagId -> {

                        RecipeTag rt = new RecipeTag();
                        rt.setRecipe(saved);
                        rt.setTag(tagRepo.getReferenceById(tagId));

                        return rt;

                    }).collect(Collectors.toList());

            recipeTagRepo.saveAll(newTags);
            saved.setRecipeTags(newTags);
        }

        return mapper.map(saved, RecipeDTO.class);
    }


    @Transactional
    public void delete(UUID id) {

        if (!recipeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Recipe not found");
        }

        recipeRepo.deleteById(id);
    }
}
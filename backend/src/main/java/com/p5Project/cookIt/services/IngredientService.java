package com.p5Project.cookIt.services;


import com.p5Project.cookIt.dtos.IngredientDTO;
import com.p5Project.cookIt.entities.Ingredient;
import com.p5Project.cookIt.mappers.IngredientMapper;
import com.p5Project.cookIt.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public List<IngredientDTO> getAll() {
        return ingredientMapper.toDTOList(ingredientRepository.findAll());
    }

    public List<IngredientDTO> search(String query) {
        return ingredientMapper.toDTOList(ingredientRepository.findAll().stream().filter(i -> i.getName().toLowerCase().contains(query.toLowerCase())).toList());
    }

    public IngredientDTO create(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);

        ingredientRepository.save(ingredient);

        return ingredientMapper.toDTO(ingredient);
    }
}
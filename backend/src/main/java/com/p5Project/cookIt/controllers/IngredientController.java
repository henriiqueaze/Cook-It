package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.IngredientControllerDocs;
import com.p5Project.cookIt.dtos.IngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateIngredientRequest;
import com.p5Project.cookIt.services.IngredientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ingredientes", description = "Gerenciamento de ingredientes")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController implements IngredientControllerDocs {

    private final IngredientService ingredientService;

    @GetMapping
    @Override
    public List<IngredientDTO> getAll() {
        return ingredientService.getAll();
    }

    @GetMapping("/search")
    @Override
    public List<IngredientDTO> search(@RequestParam String q) {
        return ingredientService.search(q);
    }

    @PostMapping
    @Override
    public IngredientDTO create(@Valid @RequestBody CreateIngredientRequest request) {
        return ingredientService.create(request.name());
    }
}
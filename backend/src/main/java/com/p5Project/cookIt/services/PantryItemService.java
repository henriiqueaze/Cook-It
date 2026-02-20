package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.pantryItem.CreatePantryItemDTO;
import com.p5Project.cookIt.models.dtos.pantryItem.PantryItemDTO;
import com.p5Project.cookIt.models.entities.PantryItem;
import com.p5Project.cookIt.repositories.IngredientRepository;
import com.p5Project.cookIt.repositories.PantryItemRepository;
import com.p5Project.cookIt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PantryItemService {

    private final PantryItemRepository repo;
    private final UserRepository userRepo;
    private final IngredientRepository ingredientRepo;
    private final ModelMapper mapper;

    public PantryItemService(PantryItemRepository repo, UserRepository userRepo, IngredientRepository ingredientRepo, ModelMapper mapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.ingredientRepo = ingredientRepo;
        this.mapper = mapper;
    }

    @Transactional
    public PantryItemDTO create(CreatePantryItemDTO dto) {
        PantryItem p = mapper.map(dto, PantryItem.class);
        if (dto.getUserId() != null) p.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getIngredientId() != null) p.setIngredient(ingredientRepo.getReferenceById(dto.getIngredientId()));
        PantryItem saved = repo.save(p);
        return mapper.map(saved, PantryItemDTO.class);
    }

    @Transactional(readOnly = true)
    public PantryItemDTO findById(UUID id) {
        PantryItem e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PantryItem not found"));
        return mapper.map(e, PantryItemDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PantryItemDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, PantryItemDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public PantryItemDTO update(UUID id, CreatePantryItemDTO dto) {
        PantryItem ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PantryItem not found"));
        mapper.map(dto, ent);
        if (dto.getUserId() != null) ent.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getIngredientId() != null) ent.setIngredient(ingredientRepo.getReferenceById(dto.getIngredientId()));
        PantryItem saved = repo.save(ent);
        return mapper.map(saved, PantryItemDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("PantryItem not found");
        repo.deleteById(id);
    }
}

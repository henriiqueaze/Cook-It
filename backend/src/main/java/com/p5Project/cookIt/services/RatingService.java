package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.rating.CreateRatingDTO;
import com.p5Project.cookIt.models.dtos.rating.RatingDTO;
import com.p5Project.cookIt.models.entities.Rating;
import com.p5Project.cookIt.repositories.RatingRepository;
import com.p5Project.cookIt.repositories.RecipeRepository;
import com.p5Project.cookIt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository repo;
    private final UserRepository userRepo;
    private final RecipeRepository recipeRepo;
    private final ModelMapper mapper;

    public RatingService(RatingRepository repo, UserRepository userRepo, RecipeRepository recipeRepo, ModelMapper mapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.recipeRepo = recipeRepo;
        this.mapper = mapper;
    }

    @Transactional
    public RatingDTO create(CreateRatingDTO dto) {
        Rating r = mapper.map(dto, Rating.class);
        if (dto.getUserId() != null) r.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getRecipeId() != null) r.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Rating saved = repo.save(r);
        return mapper.map(saved, RatingDTO.class);
    }

    @Transactional(readOnly = true)
    public RatingDTO findById(UUID id) {
        Rating r = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        return mapper.map(r, RatingDTO.class);
    }

    @Transactional(readOnly = true)
    public List<RatingDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, RatingDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public RatingDTO update(UUID id, CreateRatingDTO dto) {
        Rating ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        mapper.map(dto, ent);
        if (dto.getUserId() != null) ent.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getRecipeId() != null) ent.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Rating saved = repo.save(ent);
        return mapper.map(saved, RatingDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Rating not found");
        repo.deleteById(id);
    }
}

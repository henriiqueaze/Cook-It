package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.image.CreateImageDTO;
import com.p5Project.cookIt.models.dtos.image.ImageDTO;
import com.p5Project.cookIt.models.entities.Image;
import com.p5Project.cookIt.repositories.ImageRepository;
import com.p5Project.cookIt.repositories.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository repo;
    private final RecipeRepository recipeRepo;
    private final ModelMapper mapper;

    public ImageService(ImageRepository repo, RecipeRepository recipeRepo, ModelMapper mapper) {
        this.repo = repo;
        this.recipeRepo = recipeRepo;
        this.mapper = mapper;
    }

    @Transactional
    public ImageDTO create(CreateImageDTO dto) {
        Image img = mapper.map(dto, Image.class);
        if (dto.getRecipeId() != null) img.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Image saved = repo.save(img);
        return mapper.map(saved, ImageDTO.class);
    }

    @Transactional(readOnly = true)
    public ImageDTO findById(UUID id) {
        Image e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        return mapper.map(e, ImageDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ImageDTO> findAll() {
        return repo.findAll().stream().map(i -> mapper.map(i, ImageDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ImageDTO update(UUID id, CreateImageDTO dto) {
        Image ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        mapper.map(dto, ent);
        if (dto.getRecipeId() != null) ent.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Image saved = repo.save(ent);
        return mapper.map(saved, ImageDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Image not found");
        repo.deleteById(id);
    }
}

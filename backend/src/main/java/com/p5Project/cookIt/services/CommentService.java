package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.comment.CommentDTO;
import com.p5Project.cookIt.models.dtos.comment.CreateCommentDTO;
import com.p5Project.cookIt.models.entities.Comment;
import com.p5Project.cookIt.repositories.CommentRepository;
import com.p5Project.cookIt.repositories.RecipeRepository;
import com.p5Project.cookIt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository repo;
    private final UserRepository userRepo;
    private final RecipeRepository recipeRepo;
    private final ModelMapper mapper;

    public CommentService(CommentRepository repo, UserRepository userRepo, RecipeRepository recipeRepo, ModelMapper mapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.recipeRepo = recipeRepo;
        this.mapper = mapper;
    }

    @Transactional
    public CommentDTO create(CreateCommentDTO dto) {
        Comment c = mapper.map(dto, Comment.class);
        if (dto.getUserId() != null) c.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getRecipeId() != null) c.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Comment saved = repo.save(c);
        return mapper.map(saved, CommentDTO.class);
    }

    @Transactional(readOnly = true)
    public CommentDTO findById(UUID id) {
        Comment e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return mapper.map(e, CommentDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, CommentDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO update(UUID id, CreateCommentDTO dto) {
        Comment ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        mapper.map(dto, ent);
        if (dto.getUserId() != null) ent.setUser(userRepo.getReferenceById(dto.getUserId()));
        if (dto.getRecipeId() != null) ent.setRecipe(recipeRepo.getReferenceById(dto.getRecipeId()));
        Comment saved = repo.save(ent);
        return mapper.map(saved, CommentDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Comment not found");
        repo.deleteById(id);
    }
}

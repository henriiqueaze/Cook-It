package com.p5Project.cookIt.services;


import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.requests.CreateCommentRequest;
import com.p5Project.cookIt.dtos.requests.UpdateCommentRequest;
import com.p5Project.cookIt.entities.Comment;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.CommentMapper;
import com.p5Project.cookIt.repository.CommentRepository;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public List<CommentDTO> getRecipeComments(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        return commentMapper.toDTOList(commentRepository.findByRecipe(recipe));
    }

    public CommentDTO createComment(CreateCommentRequest request, String userId) {
        Recipe recipe = recipeRepository.findById(request.getRecipeId()).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        User user = userRepository.findById(userId).orElseThrow();

        Comment comment = new Comment();
        comment.setRecipe(recipe);
        comment.setUser(user);
        comment.setText(request.getText());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return commentMapper.toDTO(comment);
    }

    public CommentDTO updateComment(String id, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        commentMapper.updateCommentFromRequest(request, comment);
        commentRepository.save(comment);

        return commentMapper.toDTO(comment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getUserComments(String userId) {
        return commentMapper.toDTOList(commentRepository.findByUserId(userId));
    }
}
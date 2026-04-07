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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public List<CommentDTO> getRecipeComments(String recipeId) {
        ensureRecipeExists(recipeId);
        return commentMapper.toDTOList(commentRepository.findByRecipeId(recipeId));
    }

    public CommentDTO createComment(CreateCommentRequest request, String userId) {
        Comment comment = buildComment(request, userId);
        Comment saved = commentRepository.save(comment);
        return commentMapper.toDTO(saved);
    }

    public CommentDTO updateComment(String id, UpdateCommentRequest request, String userId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        ensureOwner(comment, userId);

        commentMapper.updateCommentFromRequest(request, comment);
        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public void deleteComment(String id, String userId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        ensureOwner(comment, userId);
        commentRepository.delete(comment);
    }

    public List<CommentDTO> getUserComments(String userId) {
        return commentMapper.toDTOList(commentRepository.findByUserId(userId));
    }

    private Comment buildComment(CreateCommentRequest request, String userId) {
        Comment comment = new Comment();
        comment.setRecipe(recipeRepository.findById(request.getRecipeId()).orElseThrow(() -> new ResourceNotFoundException("Recipe not found!")));
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!")));
        comment.setText(request.getText().trim());
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    private void ensureRecipeExists(String recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe not found!");
        }
    }

    private void ensureOwner(Comment comment, String userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Comment not found!");
        }
    }
}

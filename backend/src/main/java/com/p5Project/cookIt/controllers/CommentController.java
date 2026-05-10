package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.CommentControllerDocs;
import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.requests.CreateCommentRequest;
import com.p5Project.cookIt.dtos.requests.UpdateCommentRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import com.p5Project.cookIt.services.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comentários", description = "Gerenciamento de comentários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;

    @GetMapping("/recipe/{recipeId}")
    @Override
    public List<CommentDTO> getRecipeComments(@PathVariable String recipeId) {
        return commentService.getRecipeComments(recipeId);
    }

    @PostMapping
    @Override
    public CommentDTO createComment(@Valid @RequestBody CreateCommentRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return commentService.createComment(request, user.getId());
    }

    @PutMapping("/{id}")
    @Override
    public CommentDTO update(@PathVariable String id, @Valid @RequestBody UpdateCommentRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return commentService.updateComment(id, request, user.getId());
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable String id, @AuthenticationPrincipal UserPrincipal user) {
        commentService.deleteComment(id, user.getId());
    }
}
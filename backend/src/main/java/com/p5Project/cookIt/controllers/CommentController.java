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

@Tag(name = "Comentários", description = "Gerenciamento de comentários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;

    @PostMapping
    @Override
    public CommentDTO createComment(@Valid @RequestBody CreateCommentRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return commentService.createComment(request, user.getId());
    }

    @PutMapping("/{id}")
    @Override
    public CommentDTO update(@PathVariable String id, @RequestBody UpdateCommentRequest request) {
        return commentService.updateComment(id, request);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable String id) {
        commentService.deleteComment(id);
    }
}
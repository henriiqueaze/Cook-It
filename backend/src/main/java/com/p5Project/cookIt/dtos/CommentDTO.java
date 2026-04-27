package com.p5Project.cookIt.dtos;

public record CommentDTO(
        String id,
        String recipeId,
        String userId,
        String userName,
        String userPhoto,
        String text,
        String createdAt
) {
}
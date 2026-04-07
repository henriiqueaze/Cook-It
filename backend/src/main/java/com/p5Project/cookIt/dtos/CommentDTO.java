package com.p5Project.cookIt.dtos;

import lombok.Data;

@Data
public class CommentDTO {

    private String id;
    private String recipeId;
    private String userId;
    private String userName;
    private String userPhoto;
    private String text;
    private String createdAt;
}